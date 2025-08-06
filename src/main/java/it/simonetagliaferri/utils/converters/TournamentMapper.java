package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.TeamBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.domain.Team;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TournamentMapper {

    private TournamentMapper() {}

    public static TournamentBean toBean(Tournament tournament) {
        TournamentBean tournamentBean = new TournamentBean();
        List<TeamBean> confirmedTeamsBean = new ArrayList<>();
        List<TeamBean> pendingTeamsBean = new ArrayList<>();
        List<TeamBean> partialTeamsBean = new ArrayList<>();
        tournamentBean.setTournamentName(tournament.getName());
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
        if (tournament.getPendingTeams() != null) {
            for (Team team : tournament.getPendingTeams()) {
                pendingTeamsBean.add(TeamMapper.toBean(team));
            }
        }
        if (tournament.getPartialTeams() != null) {
            for (Team team : tournament.getPartialTeams()) {
                partialTeamsBean.add(TeamMapper.toBean(team));
            }
        }
        tournamentBean.setConfirmedTeams(confirmedTeamsBean);
        tournamentBean.setPendingTeams(pendingTeamsBean);
        tournamentBean.setPartialTeams(partialTeamsBean);
        tournamentBean.setJoinFee(tournament.getJoinFee());
        tournamentBean.setCourtPrice(tournament.getCourtPrice());
        return tournamentBean;
    }

    public static Tournament lightFromBean(TournamentBean tournamentBean) {
        String tournamentName = tournamentBean.getTournamentName();
        String tournamentFormat = tournamentBean.getTournamentFormat();
        String tournamentType = tournamentBean.getTournamentType();
        LocalDate startDate = tournamentBean.getStartDate();
        return new Tournament(tournamentName, tournamentFormat, tournamentType, startDate);
    }

    public static TournamentBean lightToBean(Tournament tournament) {
        TournamentBean tournamentBean = new TournamentBean();
        tournamentBean.setTournamentName(tournament.getName());
        tournamentBean.setTournamentFormat(tournament.getTournamentFormat());
        tournamentBean.setTournamentType(tournament.getTournamentType());
        tournamentBean.setStartDate(tournament.getStartDate());
        tournamentBean.setClub(ClubMapper.toBean(tournament.getClub()));
        return tournamentBean;
    }

    public static Tournament fromBean(TournamentBean tournamentBean) {
        Tournament tournament = new Tournament(tournamentBean.getTournamentName());
        if (tournamentBean.getTournamentFormat() != null) {
            tournament.setTournamentRules(tournamentBean.getTournamentFormat(),
                    tournamentBean.getTournamentType(),
                    tournamentBean.getMatchFormat(),
                    tournamentBean.getCourtType(),
                    tournamentBean.getCourtNumber(),
                    tournamentBean.getTeamsNumber());
            tournament.setTournamentDates(tournamentBean.getStartDate(), tournamentBean.getSignupDeadline(), tournamentBean.getEndDate());
            tournament.setTournamentCosts(tournamentBean.getJoinFee(), tournamentBean.getCourtPrice());
            tournament.setPrizes(tournamentBean.getPrizes());
        }
        if (tournamentBean.getClub() != null) {
            tournament.setClub(ClubMapper.fromBean(tournamentBean.getClub()));
        }
        return tournament;
    }
}
