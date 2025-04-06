package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;

public class GraphicAddTournamentControllerCLI {

    AddTournamentCLIView view = new AddTournamentCLIView();
    public void start() {
        TournamentBean tournamentBean = new TournamentBean();
        view.welcome();
        String tournamentName=view.tournamentName();
    }
}
