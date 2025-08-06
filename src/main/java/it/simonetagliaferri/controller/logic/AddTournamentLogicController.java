package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.utils.DateRules;
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
        tournamentDAO.saveTournament(club, tournament); // Saves the actual tournament.
        clubDAO.saveClub(club); // To save the updated club's tournaments list.
        hostDAO.saveHost(host); // To save the list of observed tournaments to get notifications about players joining the tournament.
        return true;
    }

    public boolean invalidTournamentName(TournamentBean tournamentBean) {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        Club club = host.getClub();
        Tournament tournament = TournamentMapper.fromBean(tournamentBean);
        return tournamentDAO.tournamentAlreadyExists(club, tournament);
    }

    public LocalDate estimatedEndDate(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Temp model to estimate the end date.
        return tournament.estimateEndDate();
    }

    public LocalDate getStartDate(TournamentBean tournamentBean, LocalDate startDate) {
        startDate = DateRules.isDateValid(startDate);
        if (startDate == null) {
            throw new InvalidDateException();
        }
        LocalDate signupDeadline = tournamentBean.getSignupDeadline();
        LocalDate endDate = tournamentBean.getEndDate();
        if (!DateRules.isStartDateValid(startDate, signupDeadline, endDate)) {
            throw new InvalidDateException();
        }
        return startDate;
    }

    public LocalDate getSignupDeadline(TournamentBean tournamentBean, LocalDate deadline) {
        deadline = DateRules.isDateValid(deadline);
        LocalDate startDate = tournamentBean.getStartDate();
        if (deadline == null || !DateRules.isDeadlineValid(deadline, startDate)) {
            throw new InvalidDateException();
        }
        return deadline;
    }

    public LocalDate getEndDate(TournamentBean tournamentBean, LocalDate endDate) {
        endDate = DateRules.isDateValid(endDate);
        LocalDate startDate = tournamentBean.getStartDate();
        if (endDate == null || !DateRules.isEndDateValid(endDate, startDate)) {
            throw new InvalidDateException();
        }
        return endDate;
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
