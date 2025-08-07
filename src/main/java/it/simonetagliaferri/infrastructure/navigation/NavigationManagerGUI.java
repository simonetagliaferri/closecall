package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.gui.GraphicHostDashboardControllerGUI;
import it.simonetagliaferri.controller.graphic.gui.GraphicPlayerDashboardControllerGUI;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Application;

import java.io.IOException;


public class NavigationManagerGUI extends NavigationManager {

    protected NavigationManagerGUI(AppContext context) {
        super(context);
    }

    private GraphicHostDashboardControllerGUI graphicHostDashboardControllerGUI;
    private GraphicPlayerDashboardControllerGUI graphicPlayerDashboardControllerGUI;

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
        if (role == Role.HOST) {
            graphicHostDashboardControllerGUI = SceneManagerGUI.hostDashboard();
        } else {
            graphicPlayerDashboardControllerGUI = SceneManagerGUI.playerDashboard();
        }
    }

    public void goToAddTournament() {
        this.graphicHostDashboardControllerGUI.showAddTournament();
    }

    public void goToAddClub() {
    }

    @Override
    public void goToInvitePlayer(TournamentBean tournamentBean) {
        this.graphicHostDashboardControllerGUI.showInvitePlayers(tournamentBean);
    }

    public void goToJoinTournament() {

    }

    @Override
    public void goToNotifications(Role role) {

    }

    @Override
    public void goToProcessInvites() {

    }


}
