package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.cli.*;
import it.simonetagliaferri.model.domain.Role;

/**
 * SceneManagerCLI implemented more than anything to be coherent with NavigationManagerGUI, in which SceneManager is needed to load FXMLs.
 * It just declutters NavigationManager from GraphicControllers' instantiation.
 */
public class SceneManagerCLI {

    public void login(AppContext appContext) {
        new GraphicLoginControllerCLI(appContext).start();
    }

    public void hostDashboard(AppContext appContext) {
        GraphicHostDashboardControllerCLI graphicHostDashboardControllerCLI = new GraphicHostDashboardControllerCLI(appContext);
        graphicHostDashboardControllerCLI.showHome();
    }

    public void playerDashboard(AppContext appContext) {
        GraphicPlayerDashboardControllerCLI graphicPlayerDashboardControllerCLI = new GraphicPlayerDashboardControllerCLI(appContext);
        graphicPlayerDashboardControllerCLI.showHome();
    }

    public void addTournament(AppContext appContext) {
        GraphicAddTournamentControllerCLI graphicAddTournamentControllerCLI = new GraphicAddTournamentControllerCLI(appContext);
        graphicAddTournamentControllerCLI.start();
    }

    public void addClub(AppContext appContext) {
        GraphicAddClubControllerCLI addClubControllerCLI = new GraphicAddClubControllerCLI(appContext);
        addClubControllerCLI.start();
    }

    public void invitePlayer(AppContext appContext, Role role, TournamentBean tournamentBean) {
        GraphicInvitePlayerControllerCLI graphicInvitePlayerControllerCLI = new GraphicInvitePlayerControllerCLI(appContext, tournamentBean);
        graphicInvitePlayerControllerCLI.start(role);
    }

    public void joinTournament(AppContext appContext) {
        GraphicJoinTournamentController graphicJoinTournamentController = new GraphicJoinTournamentController(appContext);
        graphicJoinTournamentController.start();
    }
}
