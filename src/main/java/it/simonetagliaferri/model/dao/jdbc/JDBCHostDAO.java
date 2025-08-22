package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.dao.jdbc.queries.HostQueries;
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

    TournamentDAO tournamentDAO;

    public JDBCHostDAO(TournamentDAO tournamentDAO) {
        this.tournamentDAO = tournamentDAO;
    }

    @Override
    public Host getHostByUsername(String username) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(HostQueries.findByUsername());
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
            PreparedStatement ps = conn.prepareStatement(HostQueries.getHostNotifications());
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
                PreparedStatement ps = conn.prepareStatement(HostQueries.saveHost());
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
            PreparedStatement upsert = conn.prepareStatement(HostQueries.upsertHostNotifications());
            PreparedStatement purge = conn.prepareStatement(HostQueries.deleteOldHostNotifications());
            upsert.setString(5, token);
            for (Map.Entry<Tournament, List<Player>> entry : host.getNewPlayers().entrySet()) {
                Tournament t = entry.getKey();
                upsert.setString(1, t.getClubName());
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
