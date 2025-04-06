package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.utils.CliUtils;

public class HostDashboardCLIView {
    public void hello(UserBean user) {
        CliUtils.println("Hello " + user.getUsername());
    }

    public int showMenu() {
        CliUtils.println("Welcome to your dashboard.");
        while (true) {
            CliUtils.println("1. Add tournament");
            CliUtils.println("2. Logout");
            CliUtils.println("3. Settings");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice >= 1 && choice <= 3) {
                return choice;
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }
}
