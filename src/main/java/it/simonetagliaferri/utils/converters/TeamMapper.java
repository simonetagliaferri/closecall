package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.TeamBean;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Team;

import java.util.List;

public class TeamMapper {
    public static TeamBean toBean(Team team) {
        List<Player> players = team.getPlayers();
        if (players.size() == 1) {
            return new TeamBean(PlayerMapper.toBean(players.get(0)));
        }
        else
            return new TeamBean(PlayerMapper.toBean(players.get(0)), PlayerMapper.toBean(players.get(1)));
    }
}
