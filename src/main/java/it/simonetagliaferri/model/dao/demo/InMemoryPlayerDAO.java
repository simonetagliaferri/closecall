package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;

import java.util.Map;


public class InMemoryPlayerDAO implements PlayerDAO {

    Map<String, Player> players;

    InMemoryPlayerDAO(Map<String, Player> players) {
        this.players = players;
    }

    @Override
    public Player findByUsername(String username) {
        return players.get(username);
    }

    @Override
    public Player findByEmail(String email) {
        for (Player player : players.values()) {
            if (email.equals(player.getEmail())) {
                return player;
            }
        }
        return null;
    }

    @Override
    public void savePlayer(Player player) {
        players.put(player.getUsername(), player);
    }

}
