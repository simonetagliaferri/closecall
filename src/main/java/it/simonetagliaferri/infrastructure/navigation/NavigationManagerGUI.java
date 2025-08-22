package it.simonetagliaferri.infrastructure.navigation;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.gui.GraphicHostDashboardControllerGUI;
import it.simonetagliaferri.controller.graphic.gui.GraphicPlayerDashboardControllerGUI;
import it.simonetagliaferri.exception.NavigationException;
import it.simonetagliaferri.exception.ResourceNotFoundException;
import it.simonetagliaferri.exception.ViewLoadException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Application;

public class NavigationManagerGUI extends NavigationManager {

    private GraphicHostDashboardControllerGUI graphicHostDashboardControllerGUI;
    private GraphicPlayerDashboardControllerGUI graphicPlayerDashboardControllerGUI;
    protected NavigationManagerGUI(AppContext context) {
        super(context);
    }

    /**
     * The start method calls the SceneManagerGUI's setAppContext method so that the app context can be passed to the graphic controller.
     */
    @Override
    public void start() {
        SceneManagerGUI.setAppContext(appContext);
        Application.launch(SceneManagerGUI.class);
    }

    /**
     * Navigates to the login screen.
     */
    @Override
    public void login() {
        SceneManagerGUI.login();
    }

    @Override
    public void goToDashboard(Role role) {
        try {
            if (role == Role.HOST) {
                graphicHostDashboardControllerGUI = SceneManagerGUI.hostDashboard();
            } else {
                graphicPlayerDashboardControllerGUI = SceneManagerGUI.playerDashboard();
            }
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load dashboard (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load dashboard (load error)", vle);
        }
    }

    @Override
    public void goToAddTournament() {
        try {
            this.graphicHostDashboardControllerGUI.showAddTournament();
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load add tournament screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load add tournament screen (load error)", vle);
        }
    }

    @Override
    public void goToAddClub() {
        try {
            this.graphicHostDashboardControllerGUI.showAddClub();
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load add club screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load add club screen (load error)", vle);
        }
    }

    @Override
    public void goToInvitePlayer(TournamentBean tournamentBean) {
        try {
            this.graphicHostDashboardControllerGUI.showInvitePlayers(tournamentBean);
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load invite players screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load invite players screen (load error)", vle);
        }
    }

    @Override
    public void goToJoinTournament() {
        try {
            this.graphicPlayerDashboardControllerGUI.showJoinTournament();
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load join tournament screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load join tournament screen (load error)", vle);
        }
    }

    @Override
    public void goToNotifications(Role role) {
        try {
            if (role == Role.HOST) {
                this.graphicHostDashboardControllerGUI.showNotifications();
            } else {
                this.graphicPlayerDashboardControllerGUI.showNotifications();
            }
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load notifications screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load notifications screen (load error)", vle);
        }
    }

    @Override
    public void goToProcessInvites() {
        try {
            this.graphicPlayerDashboardControllerGUI.showInvites();
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load invites screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load invites screen (load error)", vle);
        }
    }

    @Override
    public void goHome(Role role) {
        try {
            if (role == Role.HOST) {
                this.graphicHostDashboardControllerGUI.showHome();
            } else {
                this.graphicPlayerDashboardControllerGUI.showHome();
            }
        } catch (ResourceNotFoundException rnfe) {
            throw new NavigationException("Failed to load home screen (resource missing)", rnfe);
        } catch (ViewLoadException vle) {
            throw new NavigationException("Failed to load home screen (load error)", vle);
        }
    }

}
