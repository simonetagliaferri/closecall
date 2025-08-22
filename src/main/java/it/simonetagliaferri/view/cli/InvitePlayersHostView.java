package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class InvitePlayersHostView {

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

    public void playerAlreadyInvited() {
        CliUtils.println("The player has already been invited to the tournament.");
    }

    public void teamDeleted() {
        CliUtils.println("The whole team has been discarded. Please start again.");
    }

    public String getPlayer() {
        return CliUtils.prompt("Enter the player username or email: ");
    }

    public void invalidExpireDate() {
        CliUtils.println("Invalid expire date entered. The expire date must be the same day or earlier than the signup deadline.");
    }

    public void invalidDate() {
        CliUtils.println("Invalid format date. Try again.");
    }

    public void fullTournament() {
        CliUtils.println("There are no more available spots in the tournament.");
    }

    public void noSpaceForTeam() {
        CliUtils.println("You do not have any space to add a full team.");
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

    public enum InviteChoices {
        YES,
        NO
    }
}
