package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.time.LocalDate;

public class AddTournamentLogicController extends LogicController {

    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;

    public AddTournamentLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, HostDAO hostDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;
    }

    public boolean addTournament(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Creating the tournament model.
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        Club club = host.getClub();
        if (club == null) { return false; }
        if (!club.addTournament(tournament)) {return false;} // If club returns false the tournament already exists.
        clubDAO.saveClub(club);
        tournamentDAO.saveTournament(club, tournament);
        hostDAO.saveHost(host);
        return true;
    }

    public LocalDate estimatedEndDate(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Temp model to estimate the end date.
        return tournament.estimateEndDate();
    }

    public LocalDate getStartDate(TournamentBean tournamentBean, LocalDate startDate) {
        startDate = tournamentBean.isDateValid(startDate);
        if (startDate == null) {
            throw new InvalidDateException();
        }
        if (!tournamentBean.isStartDateValid(startDate)) {
            throw new InvalidDateException();
        }
        return startDate;
    }

    public LocalDate getSignupDeadline(TournamentBean tournamentBean, LocalDate deadline) {
        deadline = tournamentBean.isDateValid(deadline);
        if (deadline == null || !tournamentBean.isDeadlineValid(deadline)) {
            throw new InvalidDateException();
        }
        return deadline;
    }

    public LocalDate getEndDate(TournamentBean tournamentBean, LocalDate endDate) {
        endDate = tournamentBean.isDateValid(endDate);
        if (endDate == null || !tournamentBean.isEndDateValid(endDate)) {
            throw new InvalidDateException();
        }
        return endDate;
    }

    public LocalDate MinimumStartDate() {
        return LocalDate.now().plusDays(2);
    }

    public LocalDate MinimumStartDate(TournamentBean tournamentBean) {
        return tournamentBean.getSignupDeadline().plusDays(1);
    }

    public LocalDate MinimumDeadline() {
        return LocalDate.now().plusDays(1);
    }

    public LocalDate MinimumDeadline(TournamentBean tournamentBean) {
        return tournamentBean.getStartDate().minusDays(1);
    }

    public LocalDate MinimumEndDate(TournamentBean tournamentBean) {
        return tournamentBean.getStartDate();
    }

}
