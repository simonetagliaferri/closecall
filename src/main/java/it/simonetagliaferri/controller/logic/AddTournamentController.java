package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategyFactory;

import java.io.IOException;
import java.time.LocalDate;

public class AddTournamentController {
    Tournament tournament = new Tournament();
    SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    PlayerDAO playerDAO;
    User user = sessionManager.getCurrentUser();
    Host host = new Host(user.getUsername(), user.getEmail());
    public AddTournamentController() {
        try {
            tournamentDAO = DAOFactory.getDAOFactory().getTournamentDAO();
            hostDAO = DAOFactory.getDAOFactory().getHostDAO();
            playerDAO = DAOFactory.getDAOFactory().getPlayerDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTournament(TournamentBean tournamentBean) {
        host.setTournaments(tournamentDAO.getTournaments(host));
        tournament.setStats(tournamentBean);
        TournamentFormatStrategy strategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(tournament.getTournamentType());
        tournament.setTournamentFormatStrategy(strategy);
        host.addTournament(tournament);
        tournamentDAO.addTournament(host, tournament);
    }

    public LocalDate EstimatedEndDate(TournamentBean tournamentBean) {
        tournament.setStats(tournamentBean);
        return tournament.estimateEndDate();
    }

    public void setStartDate(TournamentBean tournamentBean, String strDate) {
        LocalDate startDate = tournamentBean.isDateValid(strDate);
        if (startDate == null) {
            throw new InvalidDateException();
        }
        tournamentBean.setStartDate(startDate);
    }

    public void setSignupDeadline(TournamentBean tournamentBean, String strDate) {
        LocalDate deadline = tournamentBean.isDateValid(strDate);
        if (deadline == null || !tournamentBean.isDeadlineValid(deadline)) {
            throw new InvalidDateException();
        }
        tournamentBean.setSignupDeadline(deadline);
    }

    public void setEndDate(TournamentBean tournamentBean, String strDate) {
        LocalDate endDate = tournamentBean.isDateValid(strDate);
        if (endDate == null || !tournamentBean.isEndDateValid(endDate)) {
            throw new InvalidDateException();
        }
        tournamentBean.setEndDate(endDate);
    }

    public HostBean getHostBean() {
        return new HostBean(sessionManager.getCurrentUser().getUsername());
    }

    public boolean firstTime() {
        return hostDAO.additionalInfo(host);
    }


    public boolean addPlayer(String player) {
        Player p = isPlayerValid(player);
        if (p == null) return false;
        if (tournament.isSingles())
            tournament.addTeam(p);
        return true;
    }

    public void addTeam(String player1, String player2) {
        Player p1 = isPlayerValid(player1);
        Player p2 = isPlayerValid(player2);
        tournament.addTeam(p1, p2);
    }


    public Player isPlayerValid(String player) {
        PlayerBean playerBean = new PlayerBean();
        playerBean.setEmail(player);
        Player p;
        if (playerBean.validEmail()) {
            p = playerDAO.findByEmail(playerBean.getEmail());
        }
        else {
            playerBean.setEmail(null);
            playerBean.setUsername(player);
            p = playerDAO.findByUsername(player);
        }
        return p;
    }
}
