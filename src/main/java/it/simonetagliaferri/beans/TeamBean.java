package it.simonetagliaferri.beans;

import java.util.ArrayList;
import java.util.List;

public class TeamBean {
    private List<PlayerBean> players;

    public TeamBean(PlayerBean player) {
        players = new ArrayList<>();
        players.add(player);
    }
    public TeamBean(PlayerBean player1, PlayerBean player2) {
        players = new ArrayList<>();
        players.add(player1);
        players.add(player2);
    }

    public List<PlayerBean> getPlayers() {
        return players;
    }
}
