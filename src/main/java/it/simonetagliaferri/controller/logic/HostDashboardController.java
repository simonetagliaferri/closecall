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
import java.util.ArrayList;
import java.util.List;

public class HostDashboardController extends Controller {
    SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();
    User user = sessionManager.getCurrentUser();
    Host host = new Host(user.getUsername(), user.getEmail());
    TournamentDAO tournamentDAO;

    public HostDashboardController() {
        try {
            tournamentDAO = DAOFactory.getDAOFactory().getTournamentDAO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
    Will have something like a getHostInfo method to get the host's proprietary attributes from a DAO.
    Also, a method to check, from the DAO, if it's the first login, if it is the required extra infos will be asked,
    just the first time, the other times the flag I suppose will be set to 1 in persistence and so nothing will be asked
     */

    public void logout() {
        sessionManager.clearSession();
    }

    public HostBean getHostBean() {
        return new HostBean(host.getUsername(), host.getEmail());
    }

    public List<TournamentBean> getTournaments() {
        List<Tournament> tournaments = tournamentDAO.getTournaments(host.getUsername());
        List<TournamentBean> tournamentBeans = new ArrayList<>();
        for (Tournament tournament : tournaments) {
            TournamentBean tournamentBean = new TournamentBean();
            tournamentBean.setTournamentName(tournament.getTournamentName());
            tournamentBeans.add(tournamentBean);
        }
        return tournamentBeans;
    }
}
