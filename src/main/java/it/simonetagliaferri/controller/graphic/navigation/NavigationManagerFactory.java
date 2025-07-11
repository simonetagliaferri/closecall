package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.controller.graphic.UIMode;

import static it.simonetagliaferri.utils.PropertiesUtils.*;

public class NavigationManagerFactory {

    private NavigationManager instance;

    public NavigationManagerFactory() {}

    // Returns the appropriate NavigationManager implementation based on ui.properties config.
    public NavigationManager getNavigationManager(AppContext context) {
        if (instance == null) {
            UIMode uiMode = loadProperty(UI_PROPERTIES, UI_KEY, UIMode.class);
            switch (uiMode) {
                case CLI:
                    instance = new NavigationManagerCLI(context);
                    break;
                case GUI:
                    instance = new NavigationManagerGUI(context);
                    break;
                default:
                    // Throw exception
            }
        }
        return instance;
    }
}
