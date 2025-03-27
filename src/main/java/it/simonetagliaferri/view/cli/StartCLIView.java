package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;

public class StartCLIView {
    public int showMenu() throws IOException {
        System.out.println("Welcome to the CLI Application");
        while (true) {
            System.out.println("1. Start");
            System.out.println("2. Exit");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice == 1 || choice == 2) {
                return choice;
            }
            System.out.println("Invalid choice. Try again.");
        }
    }

    public void end() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
