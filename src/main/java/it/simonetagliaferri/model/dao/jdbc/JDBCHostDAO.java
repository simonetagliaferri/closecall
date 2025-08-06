package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCHostDAO implements HostDAO {

    private static final String FIND_BY_USERNAME = "SELECT username, email " +
            "FROM hosts " +
            "WHERE username = ?";

    private static final String SAVE_HOST = "INSERT INTO hosts (username, email) VALUES (?, ?)";
    private static final String SAVE_NOTIFICATIONS = "INSERT INTO host_new_players(clubName, " +
            "host, tournamentName, player) VALUES (?, ?, ?, ?)";




    @Override
    public Host getHostByUsername(String username) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(FIND_BY_USERNAME);
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Host(
                            rs.getString("username"),
                            rs.getString("email")
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error in login procedure: " + e.getMessage());
        }
    }

    @Override
    public void saveHost(Host host) {
        String username = host.getUsername();
        String email = host.getEmail();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_HOST);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error in signup procedure: " + e.getMessage());
        }
        if (host.getNewPlayers() != null) {
            saveNotifications(host);
        }
    }

    private void saveNotifications(Host host) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_NOTIFICATIONS);
            for (Map.Entry<Tournament, List<Player>> entry : host.getNewPlayers().entrySet()) {
                Tournament t = entry.getKey();
                for (Player p : entry.getValue()) {
                    ps.setString(1, host.getUsername());
                    ps.setString(2, t.getClub().getName());
                    ps.setString(3, t.getName());
                    ps.setString(4, p.getUsername());
                    ps.addBatch();
                }
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error in signup procedure: " + e.getMessage());
        }
    }
}
