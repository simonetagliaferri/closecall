package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.logic.HostDashboardController;
import it.simonetagliaferri.view.cli.HostDashboardCLIView;

public class GraphicHostDashboardControllerCLI {
    HostDashboardCLIView view = new HostDashboardCLIView();
    UserBean currentUser = SessionManager.getCurrentUser();
    HostDashboardController controller = new HostDashboardController();

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

    }

    private void logout() {


    }

    private void settings() {

    }
}
