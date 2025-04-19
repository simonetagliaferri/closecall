package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.model.dao.DAOFactory;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.domain.User;

import java.io.IOException;
import java.time.LocalDate;

public class AddTournamentController {
    Tournament tournament;
    SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();
    TournamentDAO tournamentDAO;
    User user = sessionManager.getCurrentUser();
    Host host = new Host(user.getUsername(), user.getEmail());
    public AddTournamentController() {
        try {
            tournamentDAO = DAOFactory.getDAOFactory().getTournamentDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTournament(TournamentBean tournamentBean) {
        host.setTournaments(tournamentDAO.getTournaments(user.getUsername()));
        tournament = new Tournament(tournamentBean);
        host.addTournament(tournament);
        tournamentDAO.addTournament(host, tournament);
    }

    public LocalDate EstimatedEndDate(TournamentBean tournamentBean) {
        tournament = new Tournament(tournamentBean);
        return tournament.estimateEndDate();
    }

    public HostBean getHostBean() {
        return new HostBean(sessionManager.getCurrentUser().getUsername());
    }
}
