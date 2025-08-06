package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.*;
import it.simonetagliaferri.utils.CliUtils;

import java.util.List;

public class HostDashboardCLIView {

    public enum HostDashboardCommand {
        ADD_TOURNAMENT,
        LIST_TOURNAMENTS,
        CLUB_INFO,
        LOGOUT,
        NOTIFICATIONS
    }

    public void hello(UserBean user) {
        CliUtils.println("Hello " + user.getUsername());
    }

    public HostDashboardCommand showMenu() {
        CliUtils.println("Welcome to your dashboard.");
        while (true) {
            CliUtils.println("1. Add tournament");
            CliUtils.println("2. List tournaments");
            CliUtils.println("3. Club info");
            CliUtils.println("4. Logout");
            CliUtils.println("5. Notifications");
            int choice = CliUtils.promptPositiveInt("Enter your choice: ");
            if (choice >= 1 && choice <= 5) {
                return HostDashboardCommand.values()[choice-1];
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }

    public void listTournaments(List<TournamentBean> tournaments) {
        for (TournamentBean tournamentBean : tournaments) {
            CliUtils.println("Tournament " + tournamentBean.getTournamentName());
            CliUtils.println("Club " + tournamentBean.getClub().getName());
            CliUtils.println("Join fee " + tournamentBean.getJoinFee());
            CliUtils.println("Court price " + tournamentBean.getCourtPrice());
            CliUtils.println("Teams: ");
            for (TeamBean teamBean : tournamentBean.getConfirmedTeams()) {
                for (PlayerBean playerBean : teamBean.getPlayers()) {
                    CliUtils.println("\t" + playerBean.getUsername());
                }
            }
            CliUtils.println("");
        }
    }

    public void clubInfo(ClubBean clubBean) {
        CliUtils.println("Club name: " + clubBean.getName());
        CliUtils.println("Address: " + clubBean.getStreet() + ", " + clubBean.getCity() + ", " + clubBean.getZip());
        CliUtils.println("State: " + clubBean.getState() + ", " + clubBean.getCountry());
        CliUtils.println("");
    }

    public void noTournaments() {
        CliUtils.println("No tournaments found.");
    }

}
