package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.JoinTournamentLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.JoinTournamentView;

import java.util.List;

public class GraphicJoinTournamentController extends GraphicController {

    JoinTournamentLogicController controller;
    JoinTournamentView view;
    TournamentBean tournamentBean;

    public GraphicJoinTournamentController(AppContext appContext) {
        super(appContext);
        this.controller = new JoinTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO());
        this.view = new JoinTournamentView();
    }

    public void start() {
        List<TournamentBean> tournaments = this.controller.searchTournament(view.tournamentByCity());
        view.listTournaments(tournaments);
    }

}
