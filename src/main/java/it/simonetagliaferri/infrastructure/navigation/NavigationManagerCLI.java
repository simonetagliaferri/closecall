package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerCLI;
import it.simonetagliaferri.model.domain.Role;

public class NavigationManagerCLI extends NavigationManager {

    private final SceneManagerCLI sceneManager = new SceneManagerCLI();

    protected NavigationManagerCLI(AppContext appContext) {
        super(appContext);
    }

    public void login() {
        sceneManager.login(this.appContext);
    }

    public void start() {
        login();
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

    public void goToAddClub() {
        sceneManager.addClub(this.appContext);
    }

}
