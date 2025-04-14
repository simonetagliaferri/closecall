package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;

public class HostDashboardController extends Controller {
    SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();
    NavigationManager navigationManager = NavigationManager.getInstance();
    User user = sessionManager.getCurrentUser();
    Host host = new Host(user.getUsername(), user.getEmail());

    /*
    Will have something like a getHostInfo method to get the host's proprietary attributes from a DAO.
    Also, a method to check, from the DAO, if it's the first login, if it is the required extra infos will be asked,
    just the first time, the other times the flag I suppose will be set to 1 in persistence and so nothing will be asked
     */

    public void logout() {
        navigationManager.login();
        sessionManager.clearSession();
    }

    public HostBean getHostBean() {
        return new HostBean(host.getUsername(), host.getEmail());
    }
}
