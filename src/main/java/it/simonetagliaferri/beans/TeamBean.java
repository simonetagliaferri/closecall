package it.simonetagliaferri.beans;

import it.simonetagliaferri.model.domain.TeamType;

import java.util.ArrayList;
import java.util.List;

public class TeamBean {
    private final PlayerBean player1;
    private PlayerBean player2;
    private final TeamType type;

    public TeamBean(PlayerBean player, TeamType type) {
        this.player1 = player;
        this.player2 = null;
        this.type = type;
    }
    public TeamBean(PlayerBean player1, PlayerBean player2) {
        this.player1 = player1;
        this.player2 = player2;
        this.type = TeamType.DOUBLE;
    }

    public List<PlayerBean> getPlayers() {
        List<PlayerBean> players = new ArrayList<>();
        players.add(player1);
        if (type == TeamType.DOUBLE) {
            players.add(player2);
        }
        return players;
    }

    public TeamType getType() {
        return type;
    }
}
