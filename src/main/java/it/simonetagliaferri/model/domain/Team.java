package it.simonetagliaferri.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Team {
    private Player player1;
    private Player player2;
    private final TeamType type;

    public Team(Player player, TeamType type) {
        this.player1 = player;
        this.player2 = null;
        this.type = type;
    }
    public Team(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.type = TeamType.DOUBLE;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(player1);
        if (type == TeamType.DOUBLE) players.add(player2);
        return players;
    }

    public Player getPlayer() {
        return player1 != null ? player1 : player2;
    }

    public boolean isFull() {
        return (player1 != null && player2 != null && this.type == TeamType.DOUBLE) || (player1 != null && this.type == TeamType.SINGLE);
    }

    public void addPlayer(Player player) {
        if (isFull()) {
            throw new IllegalStateException("Team already full.");
        }
        else if (this.player1 == null) {
            this.player1 = player;
        }
        else if (this.player2 == null) {
            this.player2 = player;
        }
    }

    public Player getOtherPlayer(String player) {
        if (player1.getUsername().equals(player)) {
            return player2;
        }
        else if (player2.getUsername().equals(player)) {
            return player1;
        }
        return null;
    }

    public void removePlayer(Player player) {
        if (player2.getUsername().equals(player.getUsername())) {
            this.player2=null;
        }
        else if (player1.getUsername().equals(player.getUsername())) {
            this.player1=null;
        }
    }

    public TeamType getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Team)) return false;
        Team team = (Team) o;
        if (this == team) return true;
        return Objects.equals(player1, team.player1) && Objects.equals(player2, team.player2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player1, player2);
    }
}
