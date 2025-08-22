package it.simonetagliaferri.beans;

import it.simonetagliaferri.model.domain.TeamType;

import java.util.ArrayList;
import java.util.List;

public class TeamBean {
    private final PlayerBean player1;
    private final PlayerBean player2;
    private final TournamentBean tournament;
    private final TeamType type;

    public TeamBean(PlayerBean player, TeamType type, TournamentBean tournament) {
        this.player1 = player;
        this.player2 = null;
        this.tournament = tournament;
        this.type = type;
    }

    public TeamBean(PlayerBean player1, PlayerBean player2, TournamentBean tournament) {
        this.player1 = player1;
        this.player2 = player2;
        this.tournament = tournament;
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

    public TournamentBean getTournament() {
        return tournament;
    }

}
