package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.controller.graphic.cli.SceneManagerCLI;
import it.simonetagliaferri.model.domain.Role;

public class NavigationManagerCLI implements NavigationManager {

    private static final NavigationManagerCLI instance = new NavigationManagerCLI();
    private static final SceneManagerCLI sceneManager = new SceneManagerCLI();


    public static NavigationManagerCLI getInstance() {
        return instance;
    }

    public void login() {
        sceneManager.login();

    }

    public void start() {
        sceneManager.login();
    }

    public void goToDashboard(Role role) {
        if (role == Role.HOST) {
            sceneManager.hostDashboard();
        }
    }

    public void goToAddTournament() {

    }
}
