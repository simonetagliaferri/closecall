package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.controller.graphic.cli.SceneManagerCLI;
import it.simonetagliaferri.model.domain.Role;

public class NavigationManagerCLI extends NavigationManager {

    /* SceneManagerCLI implemented more than anything to be coherent with NavigationManagerGUI, in which SceneManager is needed.
        Here it just declutters NavigationManager from GraphicControllers' instantiation.
     */
    private final SceneManagerCLI sceneManager = new SceneManagerCLI();

    // Added a login operation for readability on logout calls.
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
