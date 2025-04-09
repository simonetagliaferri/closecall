package it.simonetagliaferri.controller.graphic.navigation;


import it.simonetagliaferri.controller.graphic.UIMode;
import it.simonetagliaferri.model.domain.Role;

import java.io.IOException;

import static it.simonetagliaferri.utils.PropertiesUtils.*;

public abstract class NavigationManager {
    private static NavigationManager instance;

    protected NavigationManager() {
    }

    public static synchronized NavigationManager getNavigationManager() {
        if (instance == null) {
            UIMode uiMode = null;
            try {
                uiMode = loadProperty(UI_PROPERTIES, UI_KEY, UIMode.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            switch (uiMode) {
                case CLI:
                    instance = new NavigationManagerCLI();
                    break;
                case GUI:
                    instance = new NavigationManagerGUI();
                    break;
                default:
            }
        }
        return instance;
    }

    public abstract void start();

    public abstract void login() throws IOException;

    public abstract void goToDashboard(Role role) throws IOException;

    public abstract void goToAddTournament();
}
