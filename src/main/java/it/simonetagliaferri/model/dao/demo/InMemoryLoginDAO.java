package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLoginDAO implements LoginDAO {

    private final Map<String, User> logins = new HashMap<>();

    private static InMemoryLoginDAO instance;

    private InMemoryLoginDAO() {
    }


    public static InMemoryLoginDAO getInstance() {
        if (instance == null) {
            instance = new InMemoryLoginDAO();
        }
        return instance;
    }
    @Override
    public User findByUsername(String username) {
        return logins.get(username);
    }

    @Override
    public User signup(User user) {
        logins.put(user.getUsername(), user);
        return user;
    }
}
