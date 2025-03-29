package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;

public class StartCLIView {
    public int showMenu() throws IOException {
        CliUtils.println("Welcome to the CLI Application");
        while (true) {
            CliUtils.println("1. Start");
            CliUtils.println("2. Exit");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice == 1 || choice == 2) {
                return choice;
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }

    public void end() {
        CliUtils.println("Goodbye!");
        System.exit(0);
    }
}
