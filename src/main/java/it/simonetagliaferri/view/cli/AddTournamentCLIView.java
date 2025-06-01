package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        String label = "Enter match format: ";
        String choice1 = "Best-of-three sets";
        String choice2 = "Best-of-five sets";
        return CliUtils.multipleChoice(label, choice1, choice2);
    }

    public String courtType() {
        String label = "Enter court type: ";
        String choice1 = "Hard";
        String choice2 = "Clay";
        String choice3 = "Grass";
        return CliUtils.multipleChoice(label, choice1, choice2, choice3);
    }
    public double joinFee() {
        return CliUtils.promptDouble("Enter join fee: ");
    }
    public int includedCourt() {
        String label = "Is the court cost included in the join fee?";
        String choice1 = "Yes";
        String choice2 = "No";
        return CliUtils.multipleChoiceInt(label, choice1, choice2);
    }
    public double courtCost() {
        return CliUtils.promptDouble("Enter court cost: ");
    }

    public int courtNumber() {
        return CliUtils.promptInt("Enter number of courts available for the tournament: ");
    }

    public int numberOfTeams() {
        return CliUtils.promptInt("Enter number of teams: ");
    }

    public List<Double> prizes() {
        int numOfPrizes = CliUtils.promptInt("Enter number of prizes: ");
        ArrayList<Double> prizes = new ArrayList<>(numOfPrizes);
        for (int i = 0; i < numOfPrizes; i++) {
            CliUtils.promptDouble("Enter prize " + (i + 1) + ": ");
        }
        return prizes;
    }

    public int showEstimatedEndDate(LocalDate endDate) {
        CliUtils.println("The estimated end date is: " + endDate);
        String label = "Would you like to edit the estimated end date?";
        String choice1 = "Yes";
        String choice2 = "No";
        return CliUtils.multipleChoiceInt(label, choice1, choice2);
    }

    public String editEndDate() {
        return CliUtils.prompt("Enter an estimated end date(MM/dd/yyyy): ");
    }

    public int askToAddPlayer() {
        String label = "Do you want to add a player/team to the tournament?";
        String choice1 = "Yes";
        String choice2 = "No";
        return CliUtils.multipleChoiceInt(label, choice1, choice2);
    }

    public String getPlayer() {
        return CliUtils.prompt("Enter the player username or email: ");
    }


    public String startDate() {
        return CliUtils.prompt("Enter start date(MM/dd/yyyy): ");
    }

    public String signupDeadline() {
        return CliUtils.prompt("Enter signup deadline(MM/dd/yyyy): ");
    }

    public void invalidDate() {
        CliUtils.println("Invalid date entered. Try again.");
    }

    public void invalidPlayer() {
        CliUtils.println("Player not found. Try again.");
    }
}
