package it.simonetagliaferri.controller.graphic.navigation;


import it.simonetagliaferri.controller.graphic.UIMode;
import it.simonetagliaferri.model.domain.Role;

import java.io.IOException;

import static it.simonetagliaferri.utils.PropertiesUtils.*;

public abstract class NavigationManager {
    private static NavigationManager instance;

    protected NavigationManager() {
    }
    // Returns the appropriate NavigationManager implementation based on ui.properties config.
    public static synchronized NavigationManager getInstance() {
        if (instance == null) {
            UIMode uiMode = loadProperty(UI_PROPERTIES, UI_KEY, UIMode.class);
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
