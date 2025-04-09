package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;

public class HostDashboardController {
    NavigationManager navigationManager = NavigationManager.getNavigationManager();

    public void logout() {
        try {
            navigationManager.login();
        } catch (IOException e) {
            // To review
            CliUtils.println("Error: " + e.getMessage());
        }
    }
}
