package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;

public class HostDashboardController {
    SessionManager sessionManager = SessionManager.getInstance();
    NavigationManager navigationManager = NavigationManager.getInstance();

    public void logout() {
        try {
            navigationManager.login();
        } catch (IOException e) {
            // To review
            CliUtils.println("Error: " + e.getMessage());
        }
        sessionManager.clearSession();
    }
}
