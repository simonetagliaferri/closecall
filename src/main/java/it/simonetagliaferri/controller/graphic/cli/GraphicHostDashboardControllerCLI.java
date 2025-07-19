package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HostDashboardLogicController;
import it.simonetagliaferri.view.cli.HostDashboardCLIView;

import java.util.List;

public class GraphicHostDashboardControllerCLI extends GraphicController {

    HostDashboardCLIView view;
    HostDashboardLogicController controller;
    HostBean currentUser;

    public GraphicHostDashboardControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new HostDashboardCLIView();
        this.controller = new HostDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        this.currentUser = this.controller.getHostBean();
    }

    public void showHome() {
        view.hello(currentUser);
        boolean home = true;
        if (!additionalInfo()) {
            home = false;
            logout();
        }
        while (home) {
            int choice = view.showMenu();
            switch (choice) {
                case 1:
                    addTournament();
                    break;
                case 2:
                    listTournaments();
                    break;
                case 3:
                    listClubs();
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


    private void settings() {

    }

    private void listTournaments() {
        List<TournamentBean> tournaments = this.controller.getTournaments();
        if (!tournaments.isEmpty()) {
            view.listTournaments(tournaments);
        }
        else {
            view.noTournaments();
        }
    }

    private void listClubs() {
        List<ClubBean> clubs = this.controller.getClubs();
        if (!clubs.isEmpty()) {
            view.listClubs(clubs);
        }
        else {
            view.noClubs();
        }
    }

}
