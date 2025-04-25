package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.TeamBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.domain.Team;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentMapper {

    public static TournamentBean toBean(Tournament tournament) {
        TournamentBean tournamentBean = new TournamentBean();
        List<TeamBean> teamsBean = new ArrayList<>();
        tournamentBean.setTournamentName(tournament.getTournamentName());
        tournamentBean.setTournamentType(tournament.getTournamentType());
        tournamentBean.setTournamentFormat(tournament.getTournamentFormat());
        tournamentBean.setMatchFormat(tournament.getMatchFormat());
        tournamentBean.setCourtType(tournament.getCourtType());
        tournamentBean.setCourtNumber(tournament.getCourtNumber());
        tournamentBean.setTeamsNumber(tournament.getTeamsNumber());
        tournamentBean.setPrizes(tournament.getPrizes());
        tournamentBean.setStartDate(tournament.getStartDate());
        tournamentBean.setEndDate(tournament.getEndDate());
        tournamentBean.setSignupDeadline(tournament.getSignupDeadline());
        tournamentBean.setHostUsername(tournament.getHostUsername());
        for (Team team : tournament.getTeams()) {
            teamsBean.add(TeamMapper.toBean(team));
        }
        tournamentBean.setTeams(teamsBean);
        return tournamentBean;
    }
}
