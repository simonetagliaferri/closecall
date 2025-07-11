package it.simonetagliaferri.model.dao.fs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FSLoginDAO implements LoginDAO {
    private final File file = new File("users.json");
    private final Map<String, User> users = new HashMap<>();
    private final Gson gson = new Gson();

    public FSLoginDAO() {
        loadUsers();
    }

    private void loadUsers() {
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, User>>() {
            }.getType();
            Map<String, User> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                users.clear();
                users.putAll(loaded);
            }
        } catch (IOException e) {
            CliUtils.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            CliUtils.println("Error saving users: " + e.getMessage());
        }
        loadUsers();
    }

    @Override
    public User signup(User user) {
        users.put(user.getUsername(), user);
        saveUsers();
        return user;
    }

    // Loading the users at every look up so that the user's map is always up to date.
    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equals(email)) return user;
        }
        return null;
    }
}
