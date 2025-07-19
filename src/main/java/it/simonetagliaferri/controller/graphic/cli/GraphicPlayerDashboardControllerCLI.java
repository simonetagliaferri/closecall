package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.logic.PlayerDashboardLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.view.cli.PlayerDashboardCLIView;

import java.util.List;

public class GraphicPlayerDashboardControllerCLI extends GraphicController {

    PlayerDashboardLogicController controller;
    PlayerDashboardCLIView view;

    public GraphicPlayerDashboardControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new PlayerDashboardCLIView();
        this.controller = new PlayerDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getClubDAO());
    }

    public void showHome() {
        boolean home = true;
        while (home) {
            int choice = view.showMenu();
            switch (choice) {
                case 1:
                    myTournaments();
                    break;
                case 2:
                    searchTournament();
                    break;
                case 3:
                    searchClub();
                    break;
                case 4:
                    home=false;
                    logout();
                    break;
                case 5:
                    settings();
                    break;
                default:
            }
        }
    }

    private void myTournaments() {

    }

    private void searchTournament() {

    }

    private void searchClub() {
        List<ClubBean> clubs = this.controller.searchClub(view.clubByCity());
        view.listClubs(clubs);
    }

    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    private void settings() {

    }
}
