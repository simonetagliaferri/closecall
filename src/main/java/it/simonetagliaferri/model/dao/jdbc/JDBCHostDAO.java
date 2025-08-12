package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class JDBCHostDAO implements HostDAO {

    private static final String FIND_BY_USERNAME = "SELECT username, email " +
            "FROM hosts " +
            "WHERE username = ?";

    private static final String SAVE_HOST =
            "INSERT INTO hosts (username, email) VALUES (?, ?)";

    private static final String UPSERT_NOTIFICATION =
            "INSERT INTO hostnotifications (clubName, host, tournamentName, player, batchToken) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE batchToken = VALUES(batchToken)";

    private static final String DELETE_OLD_FOR_HOST =
            "DELETE FROM hostnotifications " +
                    "WHERE host = ? AND (batchToken IS NULL OR batchToken <> ?)";

    private static final String GET_NOTIFICATIONS = "SELECT clubName, tournamentName, player FROM hostnotifications " +
            "WHERE host = ?";

    TournamentDAO tournamentDAO;
    public JDBCHostDAO(TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
    }

    @Override
    public Host getHostByUsername(String username) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME);
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Host host = new Host(
                            rs.getString("username"),
                            rs.getString("email")
                    );
                    Map<Tournament, List<Player>> newPlayers = getNotifications(host);
                    host.setNewPlayers(newPlayers);
                    return host;
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching the host: " + e.getMessage());
        }
    }

    private Map<Tournament, List<Player>> getNotifications(Host host) {
        Map<Tournament, List<Player>> newPlayers = new HashMap<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_NOTIFICATIONS);
            ps.setString(1, host.getUsername());
            ResultSet rs = ps.executeQuery();
            Tournament tournament;
            Club club;
            Player player;
            while (rs.next()) {
                club = new Club(rs.getString("clubName"), host);
                tournament = tournamentDAO.getTournament(club, rs.getString("tournamentName"));
                tournament.setClub(club);
                player = new Player(rs.getString("player"));
                List<Player> players;
                if (!newPlayers.containsKey(tournament)) {
                    players = new ArrayList<>();
                } else {
                    players = newPlayers.get(tournament);
                }
                players.add(player);
                newPlayers.put(tournament, players);
            }
            return newPlayers;
        } catch (SQLException e) {
            throw new DAOException("Error while fetching host's notifications: " + e.getMessage());
        }
    }

    @Override
    public void saveHost(Host host) {
        String username = host.getUsername();
        String email = host.getEmail();
        try {
            Connection conn = ConnectionFactory.getConnection();
            if (getHostByUsername(username) == null) {
                PreparedStatement ps = conn.prepareStatement(SAVE_HOST);
                ps.setString(1, username);
                ps.setString(2, email);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DAOException("Error while saving the host: " + e.getMessage());
        }
        if (host.getNewPlayers() != null) {
            saveNotifications(host);
        }
    }

    private void saveNotifications(Host host) {
        String token = UUID.randomUUID().toString();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement upsert = conn.prepareStatement(UPSERT_NOTIFICATION);
            PreparedStatement purge  = conn.prepareStatement(DELETE_OLD_FOR_HOST);
            upsert.setString(5, token);
            for (Map.Entry<Tournament, List<Player>> entry : host.getNewPlayers().entrySet()) {
                Tournament t = entry.getKey();
                upsert.setString(1, t.getClub().getName());
                upsert.setString(2, host.getUsername());
                for (Player p : entry.getValue()) {
                    upsert.setString(3, t.getName());
                    upsert.setString(4, p.getUsername());
                    upsert.addBatch();
                }
            }
            upsert.executeBatch();
            purge.setString(1, host.getUsername());
            purge.setString(2, token);
            purge.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error while saving host's notifications: " + e.getMessage());
        }
    }
}
