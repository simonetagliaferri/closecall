package it.simonetagliaferri.controller.graphic.cli;

public class SceneManagerCLI {
    public void login() {
        new GraphicLoginControllerCLI().start();
    }

    public void hostDashboard() {
        GraphicHostDashboardControllerCLI graphicHostDashboardControllerCLI = new GraphicHostDashboardControllerCLI();
        graphicHostDashboardControllerCLI.showHome();
    }

    public void playerDashboard() {
        GraphicPlayerDashboardControllerCLI graphicPlayerDashboardControllerCLI = new GraphicPlayerDashboardControllerCLI();
    }

    public void addTournament() {
        GraphicAddTournamentControllerCLI graphicAddTournamentControllerCLI = new GraphicAddTournamentControllerCLI();
        graphicAddTournamentControllerCLI.start();
    }

}
