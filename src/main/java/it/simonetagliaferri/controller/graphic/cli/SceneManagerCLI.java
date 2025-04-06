package it.simonetagliaferri.controller.graphic.cli;

public class SceneManagerCLI {
    public void login() {
        new GraphicLoginControllerCLI().start();
    }

    public void hostDashboard() {
        GraphicHostDashboardControllerCLI hostDashboardControllerCLI = new GraphicHostDashboardControllerCLI();
        hostDashboardControllerCLI.showHome();
    }

}
