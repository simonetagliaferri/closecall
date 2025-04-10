package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.view.cli.AddTournamentCLIView;

public class GraphicAddTournamentControllerCLI {

    AddTournamentCLIView view = new AddTournamentCLIView();
    public void start() {
        TournamentBean tournamentBean;
        view.welcome();
        String tournamentName=view.tournamentName();
        String tournamentType=view.tournamentType();
        String tournamentFormat=view.tournamentFormat();
        String matchFormat=view.matchFormat();
        tournamentBean = new TournamentBean(tournamentName,tournamentType,tournamentFormat,matchFormat);
    }
}
