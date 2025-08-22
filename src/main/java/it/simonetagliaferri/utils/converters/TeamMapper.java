package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.TeamBean;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Team;

import java.util.List;

public class TeamMapper {

    private TeamMapper() {
    }

    public static TeamBean toBean(Team team) {
        List<Player> players = team.getPlayers();
        if (players.size() == 1) {
            return new TeamBean(PlayerMapper.toBean(players.get(0)), team.getType(), TournamentMapper.lightToBean(team.getTournament()));
        } else
            return new TeamBean(PlayerMapper.toBean(players.get(0)), PlayerMapper.toBean(players.get(1)), TournamentMapper.lightToBean(team.getTournament()));
    }

}
