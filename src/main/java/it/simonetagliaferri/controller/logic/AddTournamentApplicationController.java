package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.DateRules;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.time.LocalDate;
import java.util.List;

public class AddTournamentApplicationController extends ApplicationController {

    private final TournamentDAO tournamentDAO;
    private final ClubDAO clubDAO;
    private final PlayerDAO playerDAO;

    public AddTournamentApplicationController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, PlayerDAO playerDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
        this.playerDAO = playerDAO;
    }

    private Club loadClub() {
        String username = getCurrentUserUsername();
        Club club = clubDAO.getClubByHostName(username);
        List<Tournament> tournaments = tournamentDAO.getTournaments(club);
        club.setClubTournaments(tournaments);
        return club;
    }

    public boolean addTournament(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Creating the tournament model.
        Club club = loadClub();
        if (!club.addTournament(tournament)) {
            return false;
        } // If club returns false the tournament already exists.
        club.notifySubscribers(tournament); // Notify subscribed players about the new tournament.
        tournamentDAO.saveTournament(club, tournament); // Saves the actual tournament.
        for (Player player : club.getSubscribedPlayers()) {
            playerDAO.savePlayer(player); // To save the notification of the new tournament that was sent to players with the club in their favourites.
        }
        return true;
    }

    public boolean tournamentAlreadyExists(TournamentBean tournamentBean) {
        Club club = loadClub();
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Temp model to check no duplicates.
        return club.tournamentAlreadyExists(tournament);
    }

    public LocalDate estimateEndDate(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Temp model to estimate the end date.
        return tournament.estimateEndDate();
    }

    public void assertValidStartDate(TournamentBean tournamentBean, LocalDate startDate) {
        LocalDate signupDeadline = tournamentBean.getSignupDeadline();
        LocalDate endDate = tournamentBean.getEndDate();
        if (startDate == null || !DateRules.isStartDateValid(startDate, signupDeadline, endDate)) {
            throw new InvalidDateException();
        }
    }

    public void assertValidSignupDeadline(TournamentBean tournamentBean, LocalDate deadline) {
        LocalDate startDate = tournamentBean.getStartDate();
        if (deadline == null || !DateRules.isDeadlineValid(deadline, startDate)) {
            throw new InvalidDateException();
        }
    }

    public void assertValidEndDate(TournamentBean tournamentBean, LocalDate endDate) {
        LocalDate startDate = tournamentBean.getStartDate();
        if (endDate == null || !DateRules.isEndDateValid(endDate, startDate)) {
            throw new InvalidDateException();
        }
    }

    public LocalDate minimumStartDate() {
        return DateRules.minimumStartDate();
    }

    public LocalDate minimumStartDate(TournamentBean tournamentBean) {
        LocalDate signupDeadline = tournamentBean.getSignupDeadline();
        return DateRules.minimumStartDate(signupDeadline);
    }

    public LocalDate maxDeadline() {
        return DateRules.maxDeadline();
    }

    public LocalDate maxDeadline(TournamentBean tournamentBean) {
        LocalDate startDate = tournamentBean.getStartDate();
        return DateRules.maxDeadline(startDate);
    }

    public LocalDate minimumEndDate(TournamentBean tournamentBean) {
        LocalDate startDate = tournamentBean.getStartDate();
        return DateRules.minimumEndDate(startDate);
    }

}
