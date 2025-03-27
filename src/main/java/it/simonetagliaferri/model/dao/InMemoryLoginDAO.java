package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.User;

import java.util.HashMap;
import java.util.Map;

public class InMemoryLoginDAO implements LoginDAO {

    private static InMemoryLoginDAO instance = new InMemoryLoginDAO();

    public static InMemoryLoginDAO getInstance() { return instance; }

    private InMemoryLoginDAO() {}

    private Map<String, User> logins = new HashMap<>();

    @Override
    public User login(User user) {
        if (logins.containsKey(user.getUsername())) {
            return logins.get(user.getUsername());
        }
        return null;
    }

    @Override
    public User signup(User user) {
        if (logins.containsKey(user.getUsername())) {
            return null;
        }
        logins.put(user.getUsername(), user);
        return user;
    }
}
