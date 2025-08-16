package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.logic.PlayerDashboardApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.PlayerDashboardCLIView;

import java.util.List;

public class GraphicPlayerDashboardControllerCLI extends GraphicController {

    PlayerDashboardApplicationController controller;
    PlayerDashboardCLIView view;

    public GraphicPlayerDashboardControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new PlayerDashboardCLIView();
        this.controller = new PlayerDashboardApplicationController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getClubDAO());
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
        List<TournamentBean> tournamentBeans = this.controller.getMyTournaments();
        view.listTournament(tournamentBeans);
    }

    private void searchTournament() {
        navigationManager.goToJoinTournament();
    }

    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    private void notifications() {
        if (view.chooseNotification() == PlayerDashboardCLIView.NotificationType.NOTIFICATIONS) {
            navigationManager.goToNotifications(Role.PLAYER);
        } else {
            navigationManager.goToProcessInvites();
        }
    }
}
