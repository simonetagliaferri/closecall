package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.utils.CliUtils;

import java.util.List;

public class PlayerDashboardCLIView {

    public enum PlayerDashboardCommand {
        LIST_TOURNAMENTS,
        SEARCH_TOURNAMENTS,
        SEARCH_CLUBS,
        LOGOUT,
        INVITES
    }

    public PlayerDashboardCommand showMenu() {
        CliUtils.println("Welcome to your dashboard.");
        while (true) {
            CliUtils.println("1. My tournaments");
            CliUtils.println("2. Search tournaments");
            CliUtils.println("3. Search clubs");
            CliUtils.println("4. Logout");
            CliUtils.println("5. Notifications");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice >= 1 && choice <= 5) {
                return PlayerDashboardCommand.values()[choice-1];
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }

    public String clubByCity() {
        return CliUtils.prompt("Please enter the city you would like to search clubs in: ");
    }

    public void listClubs(List<ClubBean> clubs) {
        for (ClubBean club : clubs) {
            CliUtils.println(club.getName());
            CliUtils.println(club.getStreet());
            CliUtils.println(club.getNumber());
            CliUtils.println(club.getCity());
            CliUtils.println("");
        }
    }

    public void listInvites(List<InviteBean> invites) {
        for (InviteBean invite : invites) {
            CliUtils.println(invite.getTournament().getTournamentName());
            CliUtils.println("");
        }
    }
}
