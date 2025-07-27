package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Application;

import java.io.IOException;


public class NavigationManagerGUI extends NavigationManager {

    protected NavigationManagerGUI(AppContext context) {
        super(context);
    }

    /**
     * The start method calls the SceneManagerGUI's setAppContext method so that the app context can be passed to the graphic controller.
     */
    public void start() {
        SceneManagerGUI.setAppContext(appContext);
        Application.launch(SceneManagerGUI.class);
    }

    /**
     * Navigates to the login screen.
     */
    public void login() {
        try {
            SceneManagerGUI.setRoot("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToDashboard(Role role) {
        try {
            if (role == Role.HOST) {
                SceneManagerGUI.setRoot("hostDashboard");
            } else SceneManagerGUI.setRoot("playerDashboard");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void goToAddTournament() {
    }

    public void goToAddClub() {

    }

    @Override
    public void goToInvitePlayer(Role role, TournamentBean tournamentBean) {

    }
}
