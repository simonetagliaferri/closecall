package it.simonetagliaferri.controller.graphic.navigation;


import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.UIMode;
import it.simonetagliaferri.model.domain.Role;


import static it.simonetagliaferri.utils.PropertiesUtils.*;

public abstract class NavigationManager {
    private static NavigationManager instance;
    private final SessionManager sessionManager = new SessionManager();

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

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public abstract void start();

    public abstract void login();

    public abstract void goToDashboard(Role role);

    public abstract void goToAddTournament();
}
