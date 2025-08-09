package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FSLoginDAO implements LoginDAO {

    private static final String FILE = "users.db";
    private Map<String, User> users;

    public FSLoginDAO() {
        users = new HashMap<>();
        loadUsers();
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        File file = new File(FILE);
        if (!file.exists()) return; // Start empty if no file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            users = (Map<String, User>) ois.readObject();
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading users", e);
        }
    }

    private void saveUsers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(users);
        } catch (IOException e) {
            throw new RuntimeException("Error saving users", e);
        }
    }

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

    @Override
    public void signup(User user) {
        users.put(user.getUsername(), user);
        saveUsers();
    }
}
