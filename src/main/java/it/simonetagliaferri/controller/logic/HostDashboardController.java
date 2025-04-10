package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;

public class HostDashboardController {
    SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();
    NavigationManager navigationManager = NavigationManager.getInstance();

    public void logout() {
        navigationManager.login();
        sessionManager.clearSession();
    }
}
