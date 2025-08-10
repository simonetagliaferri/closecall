package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.observer.Subscriber;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JDBCClubDAO implements ClubDAO {

    private static final String SAVE_CLUB =
            "INSERT INTO clubs (clubName, street, number, city, state, zip, country, phone, owner) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SAVE_SUBSCRIBERS =
            "INSERT INTO favouriteclubs (player, clubName, clubOwner) VALUES (?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "clubName = VALUES(clubName), " +
                    "clubOwner = VALUES(clubOwner)";

    private static final String GET_CLUB = "SELECT owner, clubName, street, number, city, state, zip, country, phone FROM clubs WHERE owner = ?";
    private static final String GET_SUBSCRIBERS = "SELECT player FROM favouriteclubs WHERE clubName = ? AND clubOwner = ?";

    @Override
    public Club getClubByHostName(String hostName) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_CLUB);
            ps.setString(1, hostName);
            ResultSet rs = ps.executeQuery();
            Club club = null;
            if (rs.next()) {
                Host host = new Host(rs.getString("owner"));
                club = new Club(rs.getString("clubName"), host);
                String street = rs.getString("street");
                String number = rs.getString("number");
                String city = rs.getString("city");
                String state = rs.getString("state");
                String zip = rs.getString("zip");
                String country = rs.getString("country");
                String phone = rs.getString("phone");
                club.updateAddress(street, number, city, state, zip, country);
                club.updateContacts(phone);
                List<Subscriber> subscribers = getSubscribersByClub(club.getName(), hostName);
                club.setSubscribers(subscribers);
            }
            return club;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Subscriber> getSubscribersByClub(String clubName, String owner) {
        List<Subscriber> subscribers = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_SUBSCRIBERS);
            ps.setString(1, clubName);
            ps.setString(2, owner);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Subscriber subscriber = new Player(rs.getString("player"));
                subscribers.add(subscriber);
            }
            return subscribers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveClub(Club club) {
        try {
            if (getClubByHostName(club.getOwner().getUsername()) == null) {
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(SAVE_CLUB);
                ps.setString(1, club.getName());
                ps.setString(2, club.getStreet());
                ps.setString(3, club.getNumber());
                ps.setString(4, club.getCity());
                ps.setString(5, club.getState());
                ps.setString(6, club.getZip());
                ps.setString(7, club.getCountry());
                ps.setString(8, club.getPhone());
                ps.setString(9, club.getOwner().getUsername());
                ps.executeUpdate();
            }
            saveSubscribers(club);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void saveSubscribers(Club club) {
        List<Player> subscribers = club.getSubscribedPlayers();
        String clubName = club.getName();
        String clubOwner = club.getOwner().getUsername();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_SUBSCRIBERS);
            for (Player p : subscribers) {
                ps.setString(1, p.getUsername());
                ps.setString(2, clubName);
                ps.setString(3, clubOwner);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
