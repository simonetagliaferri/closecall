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
        for (TournamentBean tournament : tournaments) {
            CliUtils.println("Tournament name: " + tournament.getTournamentName());
            CliUtils.println("Tournament format: " + tournament.getTournamentFormat());
            CliUtils.println("Tournament type: " + tournament.getTournamentType());
            CliUtils.println("Court type: " + tournament.getCourtType());
            CliUtils.println("Number of teams: " + tournament.getTeamsNumber());
            CliUtils.println("Available spots: " + tournament.getAvailableSpots());
            CliUtils.println("Tournament join fee: " + tournament.getJoinFee());
            String courtPrice = tournament.getCourtPrice() > 0 ? Double.toString(tournament.getCourtPrice()) : "Included in join fee";
            CliUtils.println("Court costs: " + courtPrice);
            CliUtils.print("Prizes:");
            if (!tournament.getPrizes().isEmpty()) {
                CliUtils.println("");
                for (int i = 0; i < tournament.getPrizes().size(); i++) {
                    double prize = tournament.getPrizes().get(i);
                    int place = i + 1;
                    CliUtils.println(place + ": " + prize);
                }
            }
            else CliUtils.println("none");
            CliUtils.println("Confirmed teams:");
            getPlayers(tournament.getConfirmedTeams());
            CliUtils.println("Pending teams:");
            getPlayers(tournament.getPendingTeams());
            CliUtils.println("Partial teams:");
            getPlayers(tournament.getPartialTeams());
        }
    }

    public void clubInfo(ClubBean clubBean) {
        CliUtils.println("Club name: " + clubBean.getName());
        CliUtils.println("Address: " + clubBean.getStreet() + ", "  + clubBean.getNumber() + ", " + clubBean.getCity() + ", " + clubBean.getZip());
        CliUtils.println("State: " + clubBean.getState() + ", " + clubBean.getCountry());
        CliUtils.println("");
    }

    public void noTournaments() {
        CliUtils.println("No tournaments found.");
    }

    private void getPlayers(List<TeamBean> teams) {
        for (int i = 0; i < teams.size() ; i++) {
            boolean first = true;
            for (PlayerBean playerBean : teams.get(i).getPlayers()) {
                if (playerBean != null) {
                    if (first) {
                        int number = i + 1;
                        CliUtils.println("\t" + number + ". " + playerBean.getUsername());
                        first = false;
                    } else {
                        CliUtils.println("\t    " + playerBean.getUsername());
                    }
                }
            }
        }
    }

}
