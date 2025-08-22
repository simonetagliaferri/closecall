package it.simonetagliaferri.model.dao.jdbc.queries;

public class PlayerQueries {
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

    private PlayerQueries() {
    }

    public static String findByUsername() {
        return FIND_BY_USERNAME;
    }

    public static String findByEmail() {
        return FIND_BY_EMAIL;
    }

    public static String savePlayer() {
        return SAVE_PLAYER;
    }

    public static String upsertPlayerNotification() {
        return UPSERT_PLAYER_NOTIFICATION;
    }

    public static String deleteOldPlayerNotifications() {
        return DELETE_OLD_PLAYER_NOTIFICATIONS;
    }

    public static String deleteInvite() {
        return DELETE_INVITE;
    }

    public static String getPlayerNotifications() {
        return GET_NOTIFICATIONS;
    }

    public static String saveInvites() {
        return SAVE_INVITES;
    }

    public static String getInvites() {
        return GET_INVITES;
    }
}
