package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.*;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategyFactory;
import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.time.LocalDate;
import java.util.List;

public class AddTournamentLogicController extends LogicController {
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    PlayerDAO playerDAO;
    ClubDAO clubDAO;
    public AddTournamentLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, HostDAO hostDAO, PlayerDAO playerDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;
        this.playerDAO = playerDAO;
    }

    public boolean addTournament(TournamentBean tournamentBean) {
        ClubBean clubBean = tournamentBean.getClub();
        Host host = hostDAO.getHostByUsername(getCurrentUser().getUsername());
        Club club = host.getClub(ClubMapper.fromBean(clubBean));
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Need to check for duplicates.
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
        if (tournamentAlreadyExists(tournament)) { return false; }
        tournamentDAO.addTournament(club, tournament);
        club.notifySubscribers(tournament);
        tournament.subscribe(host);
        return true;
    }

    private boolean tournamentAlreadyExists(Tournament tournament) {
        Club club = tournament.getClub();
        Host host = club.getHost();
        return club.tournamentAlreadyExists(tournament);
    }

    public LocalDate estimatedEndDate(TournamentBean tournamentBean) {
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Okay to use since it's before saving it.
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
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

    public List<ClubBean> getClubBeans() {
        List<Club> clubs = getClubs();
        return ClubMapper.toBean(clubs);
    }

    private List<Club> getClubs() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        return host.getClubs();
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
