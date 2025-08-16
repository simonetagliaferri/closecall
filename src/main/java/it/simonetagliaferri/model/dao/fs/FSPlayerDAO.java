package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FSPlayerDAO extends FSDAO implements PlayerDAO {

    private static final String FILE_NAME = "players.db";
    private final Map<String, Player> players;

    public FSPlayerDAO(Path baseDir) {
        super(baseDir, FILE_NAME);
        players = new HashMap<>();
        loadPlayers();
    }

    @SuppressWarnings("unchecked")
    private void loadPlayers() {
        Path f = file();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<String, Player> loaded = (Map<String, Player>) obj;
                players.clear();
                players.putAll(loaded);
            }
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error loading players", e);
        }
    }

    private void savePlayers() {
        Path f = file();
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(f, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(players);
        } catch (IOException e) {
            throw new DAOException("Error saving players", e);
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
