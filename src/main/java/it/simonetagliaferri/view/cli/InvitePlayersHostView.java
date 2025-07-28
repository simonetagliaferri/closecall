package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class InvitePlayersHostView {

    public enum InviteChoices {
        YES,
        NO
    }

    public String inviteExpireDate() {
        return CliUtils.prompt("Enter invite expire date(MM/dd/yyyy): ");
    }

    public InviteChoices askToAddPlayer() {
        String label = "Do you want to add a player/team to the tournament?";
        String choice1 = "Yes";
        String choice2 = "No";
        int choice = CliUtils.multipleChoiceInt(label, choice1, choice2);
        return InviteChoices.values()[choice - 1];
    }

    public String getPlayer() {
        return CliUtils.prompt("Enter the player username or email: ");
    }


    public void invalidDate() {
        CliUtils.println("Invalid date entered. Try again.");
    }

    public void invalidPlayer() {
        CliUtils.println("Player not found. Try again.");
    }

    public InviteChoices addMessage() {
        int choice = CliUtils.multipleChoiceInt("Do you want to add a message to the invite?", "Yes", "No");
        return InviteChoices.values()[choice - 1];
    }

    public void invalidPlayerUsername() {
        CliUtils.println("The inserted email/username does not correspond to a registered player.");
    }

    public String getMessage() {
        return CliUtils.prompt("Enter message: ");
    }

    public String getEmail() {
        return CliUtils.prompt("Enter email: ");
    }

    public InviteChoices askToSendEmail() {
        int choice = CliUtils.multipleChoiceInt("Do you want to send an email?", "Yes", "No");
        return InviteChoices.values()[choice - 1];
    }

    public InviteChoices askToAddTeammate() {
        int choice = CliUtils.multipleChoiceInt("Do you want to invite a teammate?", "Yes", "No");
        return InviteChoices.values()[choice - 1];
    }
}
