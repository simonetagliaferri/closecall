package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.exception.DAOException;

import java.sql.*;

public class JDBCLoginDAO implements LoginDAO {

    @Override
    public User findByUsername(String username) throws DAOException {
        String password;
        Role role;

        // Checking if the user exists, if so take the role and the password.
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call findByUsername(?)}");
            cs.setString(1, username);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                password = rs.getString("password");
                role = Role.valueOf(rs.getString("role"));
                return new User(username, password, role);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Error in login procedure: " + e.getMessage());
        }
    }

    @Override
    public User signup(User user) throws DAOException {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole().toString();

        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call signup(?,?,?,?)}");
            cs.setString(1, username);
            cs.setString(2, email);
            cs.setString(3, password);
            cs.setString(4, role);
            cs.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Error in signup procedure: " + e.getMessage());
        }
        return user;
    }
}

