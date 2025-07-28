package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.utils.CliUtils;

import java.util.List;

public class JoinTournamentView {

    public void listTournaments(List<TournamentBean> tournaments) {
        for (TournamentBean tournament : tournaments) {
            CliUtils.println(tournament.getTournamentName());
            CliUtils.println(tournament.getClub().getName());
            CliUtils.println("");
        }
    }

    public String tournamentByCity() { return CliUtils.prompt("Please enter the city you would like to enter a tournament in: "); }
}
