package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
import it.simonetagliaferri.controller.logic.HostDashboardController;
import it.simonetagliaferri.view.cli.HostDashboardCLIView;

public class GraphicHostDashboardControllerCLI {
    private final SessionManager sessionManager = NavigationManager.getInstance().getSessionManager();
    HostDashboardCLIView view = new HostDashboardCLIView();
    HostDashboardController controller = new HostDashboardController();
    HostBean currentUser = this.controller.getHostBean();

    public void showHome() {
        view.hello(currentUser);
        int choice = view.showMenu();
        switch (choice) {
            case 1:
                addTournament();
                break;
            case 2:
                this.controller.logout();
                break;
            case 3:
                settings();
                break;
            default:
        }
    }

    private void addTournament() {
        NavigationManager.getInstance().goToAddTournament();
    }


    private void settings() {

    }
}
