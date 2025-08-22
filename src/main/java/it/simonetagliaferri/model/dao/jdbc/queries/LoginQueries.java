package it.simonetagliaferri.model.dao.jdbc.queries;

public class LoginQueries {
    private static final String FIND_BY_USERNAME = "SELECT username, email, password, role " +
            "FROM users " +
            "WHERE username = ?";

    private static final String FIND_BY_EMAIL = "SELECT username, email, password, role " +
            "FROM users " +
            "WHERE email = ?";

    private static final String SIGNUP = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";

    private LoginQueries() {
    }

    public static String findByUsername() {
        return FIND_BY_USERNAME;
    }

    public static String findByEmail() {
        return FIND_BY_EMAIL;
    }

    public static String signup() {
        return SIGNUP;
    }
}
