package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.MatchBean;
import it.simonetagliaferri.beans.TeamBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.domain.Match;
import it.simonetagliaferri.model.domain.Team;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;

public class TournamentMapper {

    private TournamentMapper() {}

    public static TournamentBean toBean(Tournament tournament) {
        TournamentBean tournamentBean = new TournamentBean();
        List<TeamBean> confirmedTeamsBean = new ArrayList<>();
        List<TeamBean> reservedTeamsBean = new ArrayList<>();
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
        tournamentBean.setClub(ClubMapper.toBean(tournament.getClub()));
        if (tournament.getConfirmedTeams() != null) {
            for (Team team : tournament.getConfirmedTeams()) {
                confirmedTeamsBean.add(TeamMapper.toBean(team));
            }
        }
        if (tournament.getReservedTeams() != null) {
            for (Team team : tournament.getReservedTeams()) {
                reservedTeamsBean.add(TeamMapper.toBean(team));
            }
        }
        tournamentBean.setConfirmedTeams(confirmedTeamsBean);
        tournamentBean.setReservedTeams(reservedTeamsBean);
        List<MatchBean> matchBeans = new ArrayList<>();
        if (tournament.getMatches() != null) {
            for (Match match : tournament.getMatches()) {
                matchBeans.add(MatchMapper.toBean(match));
            }
        }
        tournamentBean.setMatches(matchBeans);
        tournamentBean.setJoinFee(tournament.getJoinFee());
        tournamentBean.setCourtPrice(tournament.getCourtPrice());
        return tournamentBean;
    }

    public static Tournament fromBean(TournamentBean tournamentBean) {
        List<Team> confirmedTeams = new ArrayList<>();
        List<Team> reservedTeams = new ArrayList<>();
        if (tournamentBean.getConfirmedTeams() != null) {
            for (TeamBean teamBean : tournamentBean.getConfirmedTeams()) {
                confirmedTeams.add(TeamMapper.fromBean(teamBean));
            }
        }

        if (tournamentBean.getReservedTeams() != null) {
            for (TeamBean teamBean : tournamentBean.getReservedTeams()) {
                reservedTeams.add(TeamMapper.fromBean(teamBean));
            }
        }
        List<Match> matches = new ArrayList<>();
        if (tournamentBean.getMatches() != null) {
            for (MatchBean matchBean : tournamentBean.getMatches()) {
                matches.add(MatchMapper.fromBean(matchBean));
            }
        }
        return new Tournament(tournamentBean.getTournamentName(), tournamentBean.getTournamentType(), tournamentBean.getTournamentFormat(),
                tournamentBean.getMatchFormat(), tournamentBean.getCourtType(), tournamentBean.getCourtNumber(), tournamentBean.getTeamsNumber(),
                tournamentBean.getPrizes(), tournamentBean.getStartDate(), tournamentBean.getEndDate(), tournamentBean.getSignupDeadline(),
                ClubMapper.fromBean(tournamentBean.getClub()), confirmedTeams, reservedTeams, matches, tournamentBean.getJoinFee(), tournamentBean.getCourtPrice());
    }
}
