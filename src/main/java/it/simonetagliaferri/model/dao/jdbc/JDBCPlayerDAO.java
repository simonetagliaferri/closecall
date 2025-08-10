package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JDBCPlayerDAO implements PlayerDAO {

    private static final String FIND_BY_USERNAME =
            "SELECT username, email FROM players WHERE username = ?";

    private static final String FIND_BY_EMAIL =
            "SELECT username, email FROM players WHERE email = ?";

    private static final String SAVE_PLAYER =
            "INSERT INTO players (username, email) VALUES (?, ?)";

    private static final String UPSERT_PLAYER_NOTIFICATION =
            "INSERT INTO playernotifications (player, clubOwner, clubName, tournamentName, batchToken) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE batchToken = VALUES(batchToken)";

    private static final String DELETE_OLD_PLAYER_NOTIFICATIONS =
            "DELETE FROM playernotifications " +
                    "WHERE player = ? AND (batchToken IS NULL OR batchToken <> ?)";

    private static final String DELETE_INVITE = "DELETE FROM invites WHERE " +
        "player = ? AND clubOwner = ? and clubName = ? and tournamentName = ?";


    private static final String GET_NOTIFICATIONS =
            "SELECT clubName, tournamentName, clubOwner FROM playernotifications WHERE player = ?";

    private static final String SAVE_INVITES =
            "INSERT INTO invites (player, clubOwner, clubName, tournamentName, sendDate, expireDate, message, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "sendDate = VALUES(sendDate), " +
                    "expireDate = VALUES(expireDate), " +
                    "message = VALUES(message), " +
                    "status  = VALUES(status)";

    private static final String GET_INVITES = "SELECT clubOwner, clubName, tournamentName, sendDate, expireDate, status, message FROM invites WHERE player = ?";

    TournamentDAO tournamentDAO;

    public JDBCPlayerDAO(TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
    }

    @Override
    public Player findByUsername(String username) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME);
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Player player = new Player(
                            rs.getString("username"),
                            rs.getString("email")
                    );
                    hydratePlayer(player);
                    return player;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error in login procedure: " + e.getMessage());
        }
    }

    @Override
    public Player findByEmail(String email) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(FIND_BY_EMAIL);
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Player player = new Player(
                            rs.getString("username"),
                            rs.getString("email")
                    );
                    hydratePlayer(player);
                    return player;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error in login procedure: " + e.getMessage());
        }
    }

    private void hydratePlayer(Player player) {
        Map<Club, List<Tournament>> notifications = getNotifications(player);
        player.setNotifications(notifications);
        List<Invite> invites = getInvites(player);
        player.setInvites(invites);
    }

    private Map<Club, List<Tournament>> getNotifications(Player player) {
        Map<Club, List<Tournament>> map = new HashMap<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_NOTIFICATIONS);
            ps.setString(1, player.getUsername());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String owner = rs.getString("clubOwner");
                    String clubName = rs.getString("clubName");
                    String tName = rs.getString("tournamentName");

                    Host clubOwner = new Host(owner); // or your Owner/User class
                    Club club = new Club(clubName, clubOwner);
                    Tournament t = tournamentDAO.getTournament(club, tName);
                    t.setClub(club);

                    map.computeIfAbsent(club, k -> new ArrayList<>()).add(t);
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching player notifications: " + e.getMessage());
        }
        return map;
    }

    private List<Invite> getInvites(Player player) {
        List<Invite> invites = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_INVITES);
            ps.setString(1, player.getUsername());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String owner = rs.getString("clubOwner");
                String clubName = rs.getString("clubName");
                String tName = rs.getString("tournamentName");
                Club club = new Club(clubName);
                club.setOwner(new Host(owner));
                Tournament t = tournamentDAO.getTournament(club, tName);
                Invite invite = new Invite(
                        t,
                        player,
                        rs.getDate("sendDate").toLocalDate(),
                        rs.getDate("expireDate").toLocalDate(),
                        InviteStatus.valueOf(rs.getString("status")),
                        rs.getString("message")
                );
                invites.add(invite);
            }
        } catch (SQLException e) {
            throw new DAOException("Error fetching invites: " + e.getMessage());
        }
        return invites;
    }

    @Override
    public void savePlayer(Player player) {
        try {
            if (findByUsername(player.getUsername()) == null) {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_PLAYER);
            ps.setString(1, player.getUsername());
            ps.setString(2, player.getEmail());
            ps.executeUpdate();
            }
            saveInvites(player, player.getInvites());
            saveNotifications(player, player.getNotifications());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveNotifications(Player player, Map<Club, List<Tournament>> notifications) {
        String playerName = player.getUsername();
        String token = java.util.UUID.randomUUID().toString();
        if (notifications == null) { return; }
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement upsert = conn.prepareStatement(UPSERT_PLAYER_NOTIFICATION);
            PreparedStatement purge = conn.prepareStatement(DELETE_OLD_PLAYER_NOTIFICATIONS);
            upsert.setString(5, token);
            for (Map.Entry<Club, List<Tournament>> e : notifications.entrySet()) {
                Club club = e.getKey();
                List<Tournament> ts = e.getValue();
                if (ts == null) continue;
                upsert.setString(2, club.getOwner().getUsername());
                upsert.setString(3, club.getName());
                for (Tournament t : ts) {
                    upsert.setString(1, playerName);
                    upsert.setString(4, t.getName());
                    upsert.addBatch();
                }
            }
            upsert.executeBatch();

            purge.setString(1, playerName);
            purge.setString(2, token);
            purge.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveInvites(Player player, List<Invite> invites) {
        if (player.getInvites() == null) return;
        String playerName = player.getUsername();
        Tournament tournament;
        Club club;
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_INVITES);
            PreparedStatement delete = conn.prepareStatement(DELETE_INVITE);
            delete.setString(1, playerName);
            ps.setString(1, playerName);
            for (Invite invite : invites) {
                tournament = invite.getTournament();
                club = tournament.getClub();
                if (invite.getStatus().equals(InviteStatus.PENDING)) {
                    ps.setString(2, club.getOwner().getUsername());
                    ps.setString(3, club.getName());
                    ps.setString(4, tournament.getName());
                    ps.setDate(5, Date.valueOf(invite.getSendDate()));
                    ps.setDate(6, Date.valueOf(invite.getExpiryDate()));
                    ps.setString(7, invite.getMessage());
                    ps.setString(8, invite.getStatus().name());
                    ps.addBatch();
                } else {
                    delete.setString(2, club.getOwner().getUsername());
                    delete.setString(3, club.getName());
                    delete.setString(4, tournament.getName());
                    delete.addBatch();
                }
            }
            ps.executeBatch();
            delete.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
