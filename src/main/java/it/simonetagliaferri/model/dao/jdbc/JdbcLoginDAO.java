package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.ConnectionFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.exception.DAOException;

import java.sql.*;

public class JdbcLoginDAO implements LoginDAO {
    private static final JdbcLoginDAO instance = new JdbcLoginDAO();

    public static JdbcLoginDAO getInstance() { return instance; }
    private JdbcLoginDAO() {}
    @Override
    public User findByUsername(User user) throws DAOException {
        String username = user.getUsername();
        String password = user.getPassword();
        Role role;

        //Ora dobbiamo ricavare il ruolo
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call findByUsername(?,?)}");
            cs.setString(1, username);
            cs.setString(2, password);
            ResultSet rs = cs.executeQuery();
            if (rs.next()) {
                username = rs.getString("username");
                password = rs.getString("password");
                role = Role.valueOf(rs.getString("role"));
                return new User(username, password, role);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new DAOException("Errore nella procedura di login: " + e.getMessage());
        }
    }

    @Override
    public User signup(User user) throws DAOException {
        String username = user.getUsername();
        String email = user.getEmail();
        String password = user.getPassword();
        String role = user.getRole().toString();

        //Ora dobbiamo ricavare il ruolo
        try {
            Connection conn = ConnectionFactory.getConnection();
            CallableStatement cs = conn.prepareCall("{call signup(?,?,?,?)}");
            cs.setString(1, username);
            cs.setString(2, email);
            cs.setString(3, password);
            cs.setString(4, role);
            cs.executeQuery();
        } catch (SQLException e) {
            throw new DAOException("Errore nella procedura di registrazione: " + e.getMessage());
        }
        return user;
    }
}

