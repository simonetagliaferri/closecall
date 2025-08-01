package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;
import java.util.Map;

public class InMemoryLoginDAO implements LoginDAO {

    private final Map<String, User> users;

    public InMemoryLoginDAO(Map<String, User> users) {
        this.users = users;
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public void signup(User user) {
        users.put(user.getUsername(), user);
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) {return user; }
        }
        return null;
    }
}
