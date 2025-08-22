package it.simonetagliaferri.model.dao.jdbc.queries;

public class ClubQueries {
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

    public static String saveClub() {
        return SAVE_CLUB;
    }

    public static String saveSubscribers() {
        return SAVE_SUBSCRIBERS;
    }

    public static String getClub() {
        return GET_CLUB;
    }

    public static String getSubscribers() {
        return GET_SUBSCRIBERS;
    }
}
