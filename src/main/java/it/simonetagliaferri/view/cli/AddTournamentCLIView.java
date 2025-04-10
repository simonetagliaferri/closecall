package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class AddTournamentCLIView {
    public void welcome() {
        CliUtils.println("Welcome to the tournament generation.");
    }

    public String tournamentName() {
        return CliUtils.prompt("Enter tournament name: ");
    }

    public String tournamentType() {
        String label = "Enter tournament type: ";
        String choice1 = "Men's singles";
        String choice2 = "Women's singles";
        String choice3 = "Men's doubles";
        String choice4 = "Women's doubles";
        String choice5 = "Mixed doubles";
        return CliUtils.multipleChoice(label, choice1, choice2, choice3, choice4, choice5);
    }

    public String tournamentFormat() {
        String label = "Enter tournament format: ";
        String choice1 = "RoundRobin";
        String choice2 = "Single-elimination";
        String choice3 = "Double-elimination";
        return CliUtils.multipleChoice(label, choice1, choice2, choice3);
    }

    public String matchFormat() {
        CliUtils.println("Enter match format: ");
        CliUtils.println("1. Best-of-three sets");
        CliUtils.println("2. Best-of-five sets");
        String label = "Enter match format: ";
        String choice1 = "Best-of-three sets";
        String choice2 = "Best-of-five sets";
        return CliUtils.multipleChoice(label, choice1, choice2);
    }
}
