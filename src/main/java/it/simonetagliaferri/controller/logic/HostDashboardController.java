package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManagerFactory;

import java.io.IOException;

public class HostDashboardController {
    NavigationManager navigationManager = NavigationManagerFactory.getInstance().getNavigationManager();

    public void logout() {
        try {
            navigationManager.login();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
