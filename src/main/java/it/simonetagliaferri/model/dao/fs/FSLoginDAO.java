package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.LoginDAO;
import it.simonetagliaferri.model.domain.User;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FSLoginDAO extends FSDAO implements LoginDAO {

    private static final String FILE_NAME = "users.db";
    private final Map<String, User> users;

    public FSLoginDAO(Path baseDir) {
        super(baseDir, FILE_NAME);
        users = new HashMap<>();
        loadUsers();
    }

    @SuppressWarnings("unchecked")
    private void loadUsers() {
        Path f = file();
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(f))) {
            Object obj = in.readObject();
            if (obj instanceof Map) {
                Map<String, User> loaded = (Map<String, User>) obj;
                users.clear();
                users.putAll(loaded);
            }
        } catch (EOFException e) {
            // empty file: start with empty map
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error loading users from " + f, e);
        }
    }

    private void saveUsers() {
        Path f = file();
        try (ObjectOutputStream out = new ObjectOutputStream(
                Files.newOutputStream(f, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            out.writeObject(users);
        } catch (IOException e) {
            throw new DAOException("Error saving users to " + f, e);
        }
    }

    @Override
    public User findByUsername(String username) {
        return users.get(username.toLowerCase());
    }

    @Override
    public User findByEmail(String email) {
        for (User user : users.values()) {
            if (user.getEmail().equalsIgnoreCase(email)) return user;
        }
        return null;
    }

    @Override
    public void signup(User user) {
        users.put(user.getUsername().toLowerCase(), user);
        saveUsers();
    }
}
