package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.controller.graphic.cli.GraphicAddTournamentControllerCLI;
import it.simonetagliaferri.controller.graphic.cli.GraphicHostDashboardControllerCLI;
import it.simonetagliaferri.controller.graphic.cli.GraphicLoginControllerCLI;
import it.simonetagliaferri.controller.graphic.cli.GraphicPlayerDashboardControllerCLI;

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
    }

    public void addTournament(AppContext appContext) {
        GraphicAddTournamentControllerCLI graphicAddTournamentControllerCLI = new GraphicAddTournamentControllerCLI(appContext);
        graphicAddTournamentControllerCLI.start();
    }

    public void addClub(AppContext appContext) {
        GraphicAddTournamentControllerCLI addTournamentControllerCLI = new GraphicAddTournamentControllerCLI(appContext);
        addTournamentControllerCLI.start();
    }
}
