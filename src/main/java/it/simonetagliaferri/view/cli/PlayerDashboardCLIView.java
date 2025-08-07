package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.utils.CliUtils;

import java.util.List;

public class PlayerDashboardCLIView {

    public enum NotificationType {
        INVITES,
        NOTIFICATIONS
    }

    public enum PlayerDashboardCommand {
        LIST_TOURNAMENTS,
        SEARCH_TOURNAMENTS,
        LOGOUT,
        INVITES
    }

    public PlayerDashboardCommand showMenu() {
        CliUtils.println("Welcome to your dashboard.");
        while (true) {
            CliUtils.println("1. My tournaments");
            CliUtils.println("2. Search tournaments by city");
            CliUtils.println("3. Logout");
            CliUtils.println("4. Notifications");
            int choice = CliUtils.promptPositiveInt("Enter your choice: ");
            if (choice >= 1 && choice <= 4) {
                return PlayerDashboardCommand.values()[choice-1];
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }

    public void listTournament(List<TournamentBean> tournaments) {
        for (TournamentBean tournament : tournaments) {
            CliUtils.println("Tournament name: " + tournament.getTournamentName());
            CliUtils.println("Club name: " + tournament.getClub().getName());
            CliUtils.println("");
        }
    }

    public NotificationType chooseNotification() {
        int choice = CliUtils.multipleChoiceInt("What notifications do you want to check?", "Invites", "New tournaments from favourite clubs");
        return NotificationType.values()[choice-1];
    }
}
