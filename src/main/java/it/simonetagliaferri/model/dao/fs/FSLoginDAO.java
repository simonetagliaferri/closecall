package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FSLoginDAO extends FSDAO implements LoginDAO {

    private final Map<String, User> users = new HashMap<>();

    public FSLoginDAO() {
        super("users.json");
        loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        if (!file.exists()) return;
        try {
            Map<String, User> loaded = mapper.readValue(file,
                    mapper.getTypeFactory().constructMapType(HashMap.class, String.class, User.class));
            if (loaded != null) {
                users.clear();
                users.putAll(loaded);
            }
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error saving users: " + e.getMessage());
        }
        loadFromFile();
    }

    @Override
    public void signup(User user) {
        reloadIfChanged();
        users.put(user.getUsername(), user);
        saveUsers();
    }

    @Override
    public User findByUsername(String username) {
        reloadIfChanged();
        return users.get(username);
    }

    @Override
    public User findByEmail(String email) {
        reloadIfChanged();
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) return user;
        }
        return null;
    }
}
