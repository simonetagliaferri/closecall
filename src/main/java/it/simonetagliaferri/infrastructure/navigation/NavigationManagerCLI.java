package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.beans.TournamentBean;
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

    /**
     * For CLI navigation the start method just calls the login method, it's here just for symmetry with the GUI navigation, in which
     * the start method calls the needed Application.launch() for JavaFX.
     */
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

    public void goToInvitePlayer(TournamentBean tournamentBean) {
        sceneManager.invitePlayer(this.appContext, tournamentBean);
    }

    public void goToProcessInvites() {
        sceneManager.goToProcessInvites(this.appContext);
    }

    public void goToJoinTournament() {
        sceneManager.joinTournament(this.appContext);
    }

    @Override
    public void goToNotifications(Role role) {
        sceneManager.goToNotifications(this.appContext, role);
    }

}
