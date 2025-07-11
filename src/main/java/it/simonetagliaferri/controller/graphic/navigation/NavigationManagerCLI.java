package it.simonetagliaferri.controller.graphic.navigation;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.controller.graphic.cli.SceneManagerCLI;
import it.simonetagliaferri.model.domain.Role;

public class NavigationManagerCLI extends NavigationManager {

    /* SceneManagerCLI implemented more than anything to be coherent with NavigationManagerGUI, in which SceneManager is needed.
        Here it just declutters NavigationManager from GraphicControllers' instantiation.
     */
    private final SceneManagerCLI sceneManager = new SceneManagerCLI();

    protected NavigationManagerCLI(AppContext appContext) {
        super(appContext);
    }

    // Added a login operation for readability on logout calls.
    public void login() {
        sceneManager.login(this.appContext);
    }

    public void start() {
        sceneManager.login(this.appContext);
    }

    public void goToDashboard(Role role) {
        if (role == Role.HOST) {
            sceneManager.hostDashboard(this.appContext);
        }
        else {
            sceneManager.playerDashboard(this.appContext);
        }
    }

    public void goToAddTournament() {
        sceneManager.addTournament(this.appContext);
    }
}
