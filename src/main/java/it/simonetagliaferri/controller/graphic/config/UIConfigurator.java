package it.simonetagliaferri.controller.graphic.config;

import it.simonetagliaferri.controller.graphic.navigation.NavigationManagerCLI;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManagerFactory;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManagerGUI;

public class UIConfigurator {
    private UIConfigurator() {}
    public static void configureUI(UIMode ui) {
        switch (ui) {
            case CLI:
                NavigationManagerFactory.getInstance().setImplClass(NavigationManagerCLI.class);
                NavigationManagerFactory.getInstance().getNavigationManager().start();
                break;
            case GUI:
                NavigationManagerFactory.getInstance().setImplClass(NavigationManagerGUI.class);
                NavigationManagerFactory.getInstance().getNavigationManager().start();
                break;
            default:
                throw new IllegalArgumentException("Unsupported UI mode: " + ui);
        }
    }
}
