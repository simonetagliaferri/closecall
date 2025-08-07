package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.controller.logic.ManageTournamentsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HostDashboardLogicController;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.HostDashboardCLIView;

import java.util.List;

public class GraphicHostDashboardControllerCLI extends GraphicController {

    HostDashboardCLIView view;
    HostDashboardLogicController controller;
    HostBean currentUser;

    ManageTournamentsLogicController tournamentController;

    public GraphicHostDashboardControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new HostDashboardCLIView();
        this.controller = new HostDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        this.currentUser = this.controller.getHostBean();

        this.tournamentController = new ManageTournamentsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());

    }

    public void showHome() {
        view.hello(currentUser);
        boolean home = true;
        if (!additionalInfo()) {
            home = false;
            logout();
        }
        while (home) {
            HostDashboardCLIView.HostDashboardCommand choice = view.showMenu();
            switch (choice) {
                case ADD_TOURNAMENT:
                    addTournament();
                    break;
                case LIST_TOURNAMENTS:
                    listTournaments();
                    break;
                case CLUB_INFO:
                    clubInfo();
                    break;
                case LOGOUT:
                    home=false;
                    logout();
                    break;
                case NOTIFICATIONS:
                    notifications();
                    break;
                default:
            }
        }
    }

    private boolean additionalInfo() {
        if (this.controller.additionalInfoNeeded()) {
            navigationManager.goToAddClub();
        }
        return !this.controller.additionalInfoNeeded();
    }

    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    private void addTournament() {
        navigationManager.goToAddTournament();
    }


    private void notifications() {
        navigationManager.goToNotifications(Role.HOST);
    }


    private void listTournaments() {
        List<TournamentBean> tournaments = this.tournamentController.getTournaments();
        if (!tournaments.isEmpty()) {
            view.listTournaments(tournaments);
        }
        else {
            view.noTournaments();
        }
    }

    private void clubInfo() {
        ClubBean club = this.controller.getClubBean();
        view.clubInfo(club);
    }

}
