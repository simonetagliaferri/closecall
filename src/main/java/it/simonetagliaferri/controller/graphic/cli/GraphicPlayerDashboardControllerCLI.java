package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.logic.PlayerDashboardLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.PlayerDashboardCLIView;

import java.util.List;

public class GraphicPlayerDashboardControllerCLI extends GraphicController {

    PlayerDashboardLogicController controller;
    PlayerDashboardCLIView view;

    public GraphicPlayerDashboardControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new PlayerDashboardCLIView();
        this.controller = new PlayerDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getClubDAO(),
                appContext.getDAOFactory().getInviteDAO());
    }

    public void showHome() {
        boolean home = true;
        while (home) {
            PlayerDashboardCLIView.PlayerDashboardCommand choice = view.showMenu();
            switch (choice) {
                case LIST_TOURNAMENTS:
                    myTournaments();
                    break;
                case SEARCH_TOURNAMENTS:
                    searchTournament();
                    break;
                case SEARCH_CLUBS:
                    searchClub();
                    break;
                case LOGOUT:
                    home=false;
                    logout();
                    break;
                case INVITES:
                    notifications();
                    break;
                default:
            }
        }
    }

    private void myTournaments() {

    }

    private void searchTournament() {
        navigationManager.goToJoinTournament();
    }

    private void searchClub() {
        List<ClubBean> clubs = this.controller.searchClub(view.clubByCity());
        view.listClubs(clubs);
    }

    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    private void notifications() {
        navigationManager.goToInvitePlayer(Role.PLAYER, null);
    }
}
