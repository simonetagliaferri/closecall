package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLoginDAO implements LoginDAO {

    private static final InMemoryLoginDAO instance = new InMemoryLoginDAO();
    private final Map<String, User> logins = new HashMap<>();

    private InMemoryLoginDAO() {
    }

    public static InMemoryLoginDAO getInstance() {
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
