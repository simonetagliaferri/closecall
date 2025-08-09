package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FSPlayerDAO implements PlayerDAO {

    private static final String FILE = "players.db";
    private Map<String, Player> players;

    public FSPlayerDAO() {
        players = new HashMap<>();
        loadPlayers();
    }

    @SuppressWarnings("unchecked")
    private void loadPlayers() {
        File file = new File(FILE);
        if (!file.exists()) return; // Start empty if no file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            players = (Map<String, Player>) ois.readObject();
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading users", e);
        }
    }

    private void savePlayers() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
            oos.writeObject(players);
        } catch (IOException e) {
            throw new RuntimeException("Error saving users", e);
        }
    }

    @Override
    public Player findByUsername(String username) {
        return players.get(username);
    }

    @Override
    public Player findByEmail(String email) {
        for (Player player : players.values()) {
            if (player.getEmail().equals(email)) return player;
        }
        return null;
    }

    @Override
    public void savePlayer(Player player) {
        players.put(player.getUsername(), player);
        savePlayers();
    }

}
