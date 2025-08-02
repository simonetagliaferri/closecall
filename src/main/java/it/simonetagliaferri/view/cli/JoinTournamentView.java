package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.utils.CliUtils;
import java.util.List;

public class JoinTournamentView {

    public enum JoinStatus {
        JOIN,
        BACK
    }

    public enum JoinError {
        NO_AVAILABLE_SPOTS,
        ALREADY_IN_A_TEAM,
        SUCCESS
    }

    public int listTournaments(List<TournamentBean> tournaments) {
        int tournamentsCount = 1;
        for (TournamentBean tournament : tournaments) {
            CliUtils.print(tournamentsCount + ": ");
            CliUtils.println(tournament.getTournamentName());
            CliUtils.println(tournament.getClub().getName());
            CliUtils.println("");
            tournamentsCount++;
        }
        return CliUtils.multipleChoiceInt(tournaments.size(), "Select the tournament you are interested in to expand it(0 to go back): ");
    }

    public String tournamentByCity() { return CliUtils.prompt("Please enter the city you would like to enter a tournament in: "); }

    public JoinStatus expandedTournament(TournamentBean tournament) {
        CliUtils.println(tournament.getTournamentName());
        CliUtils.println(tournament.getClub().getName());
        CliUtils.println(tournament.getTournamentFormat());
        CliUtils.println(tournament.getTournamentFormat());
        CliUtils.println(tournament.getTournamentFormat());
        int choice = CliUtils.multipleChoiceInt("What do you want to do?", "Join", "Go back");
        return JoinStatus.values()[choice-1];
    }

    public void success() {
        CliUtils.println("You successfully joined the tournament.");
    }

    public void alreadyJoined() {
        CliUtils.println("You are already in this tournament or you have a valid invite for it. Check your invites.");
    }

    public void noAvailableSpots() {
        CliUtils.println("There are no available spots in this tournament.");
    }

    public boolean addClubToFavourites() {
        int choice = CliUtils.multipleChoiceInt("Do you want to add the hosting club to your favourites?", "Yes", "No");
        return choice == 1;
    }
}
