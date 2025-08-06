package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AddTournamentCLIView {

    public enum Choice {
        YES,
        NO
    }

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
        return CliUtils.promptPositiveDouble("Enter join fee: ");
    }

    public Choice includedCourt() {
        String label = "Is the court cost included in the join fee?";
        String choice1 = "Yes";
        String choice2 = "No";
        int choice = CliUtils.multipleChoiceInt(label, choice1, choice2);
        return Choice.values()[choice - 1];
    }
    public double courtCost() {
        return CliUtils.promptPositiveDouble("Enter court cost: ");
    }

    public int courtNumber() {
        return CliUtils.promptPositiveInt("Enter number of courts available for the tournament: ");
    }

    public int numberOfTeams() {
        return CliUtils.promptPositiveInt("Enter number of teams: ");
    }

    public List<Double> prizes() {
        int numOfPrizes = CliUtils.promptPositiveInt("Enter number of prizes: ");
        ArrayList<Double> prizes = new ArrayList<>(numOfPrizes);
        double prize;
        for (int i = 0; i < numOfPrizes; i++) {
            prize = CliUtils.promptPositiveDouble("Enter prize " + (i + 1) + ": ");
            prizes.add(prize);
        }
        return prizes;
    }

    public Choice showEstimatedEndDate(LocalDate endDate) {
        CliUtils.println("The estimated end date is: " + endDate);
        String label = "Would you like to edit the estimated end date?";
        String choice1 = "Yes";
        String choice2 = "No";
        int choice = CliUtils.multipleChoiceInt(label, choice1, choice2);
        return Choice.values()[choice - 1];
    }

    public void tournamentAlreadyExists() {
        CliUtils.println("You already have a tournament with this name. Try again.");
    }

    public String editEndDate() {
        return CliUtils.prompt("Enter an estimated end date(MM/dd/yyyy): ");
    }

    public int getClub(List<String> clubs) {
        return CliUtils.multipleChoiceInt("Choose a club: ", clubs);
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


    public Choice askToAddPlayer() {
        String label = "Do you want to add a player/team to the tournament?";
        String choice1 = "Yes";
        String choice2 = "No";
        int choice = CliUtils.multipleChoiceInt(label, choice1, choice2);
        return Choice.values()[choice - 1];
    }
}
