package it.simonetagliaferri.model.dao.jdbc.queries;

public class HostQueries {

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

    private HostQueries() {
    }

    public static String findByUsername() {
        return FIND_BY_USERNAME;
    }

    public static String saveHost() {
        return SAVE_HOST;
    }

    public static String upsertHostNotifications() {
        return UPSERT_NOTIFICATION;
    }

    public static String deleteOldHostNotifications() {
        return DELETE_OLD_FOR_HOST;
    }

    public static String getHostNotifications() {
        return GET_NOTIFICATIONS;
    }

}
