package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class AddClubCLIView {

    public PreambleChoice preamble() {
        CliUtils.println("Hey there. It looks like it's your first time around here.");
        CliUtils.println("To be an actual host on our platform, you need to have at least one club.");
        int choice = CliUtils.multipleChoiceInt("What do you say? Are you ready to add a new club?", "Yes", "No(You will be logged out)");
        if (choice == 1) {
            return PreambleChoice.ADD_CLUB;
        }
        return PreambleChoice.NOT_ADD_CLUB;
    }

    public void newClubAdded() {
        CliUtils.println("New club successfully added.");
    }

    public void clubAlreadyExists() {
        CliUtils.println("You are trying to add a club that already exists. You can't have clubs with the same name.");
    }

    public String getClubName() {
        return CliUtils.prompt("Please enter your club name: ");
    }

    public String getClubStreet() {
        return CliUtils.prompt("Please enter your club street: ");
    }

    public String getClubNumber() {
        return CliUtils.prompt("Please enter your club number: ");
    }

    public String getClubCity() {
        return CliUtils.prompt("Please enter your club city: ");
    }

    public String getClubState() {
        return CliUtils.prompt("Please enter your club state: ");
    }

    public String getClubZip() {
        return CliUtils.prompt("Please enter your club zip: ");
    }

    public String getClubCountry() {
        return CliUtils.prompt("Please enter your club country: ");
    }

    public String getClubPhone() {
        return CliUtils.prompt("Please enter your club phone: ");
    }

    public enum PreambleChoice {
        ADD_CLUB,
        NOT_ADD_CLUB
    }
}
