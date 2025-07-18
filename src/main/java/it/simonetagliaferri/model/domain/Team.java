package it.simonetagliaferri.model.domain;

import java.util.ArrayList;
import java.util.List;

public class Team {
    // At first I was using Player[], but using a List<Player> is just more comfortable.
    private final List<Player> players;

    public Team(Player player) {
        players = new ArrayList<>();
        players.add(player);
    }
    public Team(Player player1, Player player2) {
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
    }

    public List<Player> getPlayers() {
        return players;
    }
}
