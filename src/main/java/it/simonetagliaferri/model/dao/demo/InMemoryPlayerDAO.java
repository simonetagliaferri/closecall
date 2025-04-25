package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.model.domain.User;

import java.io.IOException;

public class InMemoryPlayerDAO implements PlayerDAO {
    @Override
    public Player findByUsername(String username) {
        LoginDAO loginDAO;
        try {
            loginDAO = DAOFactory.getDAOFactory().getLoginDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = loginDAO.findByUsername(username);
        if (user.getRole() == Role.PLAYER) {
            return new Player(user.getUsername(), user.getEmail());
        }
        return null;
    }

    @Override
    public Player findByEmail(String email) {
        LoginDAO loginDAO;
        try {
            loginDAO = DAOFactory.getDAOFactory().getLoginDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        User user = loginDAO.findByEmail(email);
        if (user.getRole() == Role.PLAYER) {
            return new Player(user.getUsername(), user.getEmail());
        }
        return null;
    }
}
