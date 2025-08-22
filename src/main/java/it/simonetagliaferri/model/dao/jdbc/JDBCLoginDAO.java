package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.jdbc.queries.LoginQueries;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCLoginDAO implements LoginDAO {

    @Override
    public User findByUsername(String username) throws DAOException {
        return getUser(username, LoginQueries.findByUsername());
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
            PreparedStatement ps = conn.prepareStatement(LoginQueries.signup());
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
        return getUser(email, LoginQueries.findByEmail());
    }
}

