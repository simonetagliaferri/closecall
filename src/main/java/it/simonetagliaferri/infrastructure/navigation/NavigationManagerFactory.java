package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.exception.InvalidConfigFile;
import it.simonetagliaferri.infrastructure.AppContext;

import static it.simonetagliaferri.utils.PropertiesUtils.*;

public class NavigationManagerFactory {

    private NavigationManagerFactory() {
    }

    /**
     * Factory method for creating the correct NavigationManager implementation.
     * This method should only be called once by AppContext at application startup.
     */
    public static  NavigationManager getNavigationManager(AppContext context) {
        UIMode uiMode = loadProperty(UI_PROPERTIES, UI_KEY, UIMode.class);
        switch (uiMode) {
            case CLI:
                return new NavigationManagerCLI(context);
            case GUI:
                return new NavigationManagerGUI(context);
            default:
                throw new InvalidConfigFile("Invalid UI mode: " + uiMode);
            }
    }

}
