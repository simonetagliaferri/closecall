package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.JoinTournamentLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.JoinTournamentView;

import java.util.List;

public class GraphicJoinTournamentControllerCLI extends GraphicController {

    JoinTournamentLogicController controller;
    JoinTournamentView view;

    public GraphicJoinTournamentControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new JoinTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO(), appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO());
        this.view = new JoinTournamentView();
    }

    public void start() {
        List<TournamentBean> tournaments = this.controller.searchTournament(view.tournamentByCity());
        boolean tournamentList = true;
        while (tournamentList) {
            int choice = view.listTournaments(tournaments);
            if (choice == -1) {
                break;
            }
            TournamentBean tournamentBean = tournaments.get(choice);
            JoinTournamentView.JoinStatus status = view.expandedTournament(tournamentBean);
            JoinTournamentView.JoinError result;
            if (status.equals(JoinTournamentView.JoinStatus.BACK)) {
                break;
            }
            else {
                result = this.controller.joinTournament(tournamentBean);
            }
            if (result == JoinTournamentView.JoinError.SUCCESS) {
                view.success();
                tournamentList = false;
                if (!this.controller.isSubscribed(tournamentBean)) {
                    addClubToFavourites(tournamentBean);
                }
            }
            else if (result == JoinTournamentView.JoinError.NO_AVAILABLE_SPOTS) {
                view.noAvailableSpots();
            }
            else if (result == JoinTournamentView.JoinError.ALREADY_IN_A_TEAM) {
                view.alreadyJoined();
            }
        }
    }

    public void addClubToFavourites(TournamentBean tournamentBean) {
        if (view.addClubToFavourites()) {
            this.controller.addClubToFavourites(tournamentBean);
        }
    }
}
