package it.simonetagliaferri.model.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Team implements Serializable {

    private final Tournament tournament;
    private final TeamType type;
    private Player player1;
    private Player player2;
    private TeamStatus status;

    public Team(Player player, TeamType type, Tournament tournament) {
        this.player1 = player;
        this.player2 = null;
        this.tournament = tournament;
        this.type = type;
    }

    public Team(Player player1, Player player2, Tournament tournament) {
        this.player1 = player1;
        this.player2 = player2;
        this.tournament = tournament;
        if (this.player2 != null) {
            this.type = TeamType.DOUBLE;
        } else {
            this.type = TeamType.SINGLE;
        }
    }

    public boolean isFull() {
        return (player1 != null && player2 != null && this.type == TeamType.DOUBLE) || (player1 != null && this.type == TeamType.SINGLE);
    }

    public void addPlayer(Player player) {
        if (isFull()) {
            throw new IllegalStateException("Team already full.");
        } else if (this.player1 == null) {
            this.player1 = player;
        } else if (this.player2 == null) {
            this.player2 = player;
        }
    }

    public void removePlayer(Player player) {
        if (player2.getUsername().equals(player.getUsername())) {
            this.player2 = null;
        } else if (player1.getUsername().equals(player.getUsername())) {
            this.player1 = null;
        }
    }

    public boolean hasPlayer(Player player) {
        return player.isSameAs(player1) || player.isSameAs(player2);
    }

    public TeamType getType() {
        return type;
    }

    public List<Player> getPlayers() {
        List<Player> players = new ArrayList<>();
        players.add(player1);
        if (type == TeamType.DOUBLE) players.add(player2);
        return players;
    }

    public Player getOtherPlayer(String player) {
        if (player1.getUsername().equals(player)) {
            return player2;
        } else if (player2.getUsername().equals(player)) {
            return player1;
        }
        return null;
    }

    public TeamStatus getStatus() {
        return status;
    }

    public void setStatus(TeamStatus status) {
        this.status = status;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Player getPlayer() {
        return player1 != null ? player1 : player2;
    }

}
