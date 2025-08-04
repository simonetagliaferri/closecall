package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FSPlayerDAO extends FSDAO implements PlayerDAO {

    private final Map<String, Player> players;

    public FSPlayerDAO() {
        super("players.json");
        players = new HashMap<>();
        loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        if (!file.exists()) return;

        try {
            // Deserialize the map of hosts from JSON
            Map<String, Player> loaded = mapper.readValue(file,
                    mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Player.class));

            if (loaded != null) {
                players.clear();
                players.putAll(loaded);
            }

            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error loading players: " + e.getMessage());
        }
    }

    @Override
    public Player findByUsername(String username) {
        reloadIfChanged();
        return players.get(username);
    }

    @Override
    public void savePlayer(Player player) {
        reloadIfChanged();
        players.put(player.getUsername(), player);
        savePlayers();
    }

    private void savePlayers() {
        try {
            // Serialize the map of hosts back to JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, players);
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error saving hosts: " + e.getMessage());
        }
    }

    @Override
    public Player findByEmail(String email) {
        reloadIfChanged();
        for (Map.Entry<String, Player> entry : players.entrySet()) {
            if (entry.getValue().getEmail().equals(email)) {
                return entry.getValue();
            }
        }
        return null;
    }
}
