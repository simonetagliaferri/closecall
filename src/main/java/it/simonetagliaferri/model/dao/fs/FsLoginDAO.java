package it.simonetagliaferri.model.dao.fs;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;

import java.io.*;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class FsLoginDAO implements LoginDAO {
    private final File file = new File("users.json");
    private final Map<String, User> users = new HashMap<>();
    private final Gson gson = new Gson();
    private static final FsLoginDAO instance = new FsLoginDAO();

    public static FsLoginDAO getInstance() { return instance; }

    private FsLoginDAO() {
        loadUsers();
    }

    private void loadUsers() {
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, User>>() {}.getType();
            Map<String, User> loaded = gson.fromJson(reader, type);
            if (loaded != null) users.putAll(loaded);
        } catch (IOException e) {
            System.out.println("Error loading users: " + e.getMessage());
        }
    }

    private void saveUsers() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.out.println("Error saving users: " + e.getMessage());
        }
    }
    @Override
    public User signup(User user) {
        users.put(user.getUsername(), user);
        saveUsers();
        return user;
    }
    @Override
    public User findByUsername(String username) {
        return users.get(username);
    }

}
