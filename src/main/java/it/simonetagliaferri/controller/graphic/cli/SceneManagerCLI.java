package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.AppContext;

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

}
