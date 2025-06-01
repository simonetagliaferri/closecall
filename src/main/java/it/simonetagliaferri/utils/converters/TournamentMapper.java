package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.TeamBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.domain.Team;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentMapper {

    private TournamentMapper() {}

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
        if (tournament.getTeams() != null) {
            for (Team team : tournament.getTeams()) {
                teamsBean.add(TeamMapper.toBean(team));
            }
        }
        tournamentBean.setTeams(teamsBean);
        tournamentBean.setJoinFee(tournament.getJoinFee());
        tournamentBean.setCourtPrice(tournament.getCourtPrice());
        return tournamentBean;
    }

    public static Tournament fromBean(TournamentBean tournamentBean) {
        List<Team> teams = new ArrayList<>();
        if (tournamentBean.getTeams() != null) {
            for (TeamBean teamBean : tournamentBean.getTeams()) {
                teams.add(TeamMapper.fromBean(teamBean));
            }
        }
        return new Tournament(tournamentBean.getTournamentName(), tournamentBean.getTournamentType(), tournamentBean.getTournamentFormat(),
                tournamentBean.getMatchFormat(), tournamentBean.getCourtType(), tournamentBean.getCourtNumber(), tournamentBean.getTeamsNumber(),
                tournamentBean.getPrizes(), tournamentBean.getStartDate(), tournamentBean.getEndDate(), tournamentBean.getSignupDeadline(),
                tournamentBean.getHostUsername(), teams, tournamentBean.getJoinFee(), tournamentBean.getCourtPrice());
    }
}
