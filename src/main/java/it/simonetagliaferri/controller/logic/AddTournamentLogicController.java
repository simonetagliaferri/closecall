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
    InviteDAO inviteDAO;
    public AddTournamentLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, ClubDAO clubDAO, HostDAO hostDAO, PlayerDAO playerDAO, InviteDAO inviteDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;
        this.playerDAO = playerDAO;
        this.inviteDAO = inviteDAO;
    }

    public void addTournament(TournamentBean tournamentBean) {
        ClubBean clubBean = tournamentBean.getClub();
        Host host = hostDAO.getHostByUsername(getCurrentUser().getUsername());
        Club club = clubDAO.getClubByName(host, clubBean.getName());
        Tournament tournament = TournamentMapper.fromBean(tournamentBean); // Need to check for duplicates.
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentFormat());
        tournament.setTournamentFormatStrategy(strategy);
        tournamentDAO.addTournament(club, tournament);
        club.notifySubscribers(tournament);
    }

    public boolean tournamentAlreadyExists(TournamentBean tournamentBean) {
        ClubBean clubBean = tournamentBean.getClub();
        HostBean hostBean = clubBean.getOwner();
        Host host = hostDAO.getHostByUsername(hostBean.getUsername());
        Club club = clubDAO.getClubByName(host, clubBean.getName());
        String tournamentName = tournamentBean.getTournamentName();
        String tournamentFormat = tournamentBean.getTournamentFormat();
        String tournamentType = tournamentBean.getTournamentType();
        LocalDate startDate = tournamentBean.getStartDate();
        return tournamentDAO.getTournament(club, tournamentName, tournamentFormat, tournamentType, startDate)!=null;
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
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        List<Club> clubs = clubDAO.getClubs(host);
        return ClubMapper.toBean(clubs);
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
