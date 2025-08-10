package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.exception.DAOException;

import java.sql.*;

public class JDBCLoginDAO implements LoginDAO {

    private static final String FIND_BY_USERNAME = "SELECT username, email, password, role " +
            "FROM users " +
            "WHERE username = ?";

    private static final String FIND_BY_EMAIL = "SELECT username, email, password, role " +
            "FROM users " +
            "WHERE email = ?";

    private static final String SIGNUP = "INSERT INTO users (username, email, password, role) VALUES (?, ?, ?, ?)";

    @Override
    public User findByUsername(String username) throws DAOException {
        return getUser(username, FIND_BY_USERNAME);
    }

    private User getUser(String search, String findBy) {
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(findBy);
            ps.setString(1, search);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("password"),
                            Role.valueOf(rs.getString("role"))
                    );
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error in login procedure: " + e.getMessage());
        }
    }

    @Override
    public void signup(User user) throws DAOException {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole().toString();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SIGNUP);
            ps.setString(1, username);
            ps.setString(2, email);
            ps.setString(3, password);
            ps.setString(4, role);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Error in signup procedure: " + e.getMessage());
        }
    }

    @Override
    public User findByEmail(String email) throws DAOException {
        return getUser(email, FIND_BY_EMAIL);
    }
}

