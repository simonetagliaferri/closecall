package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class InvitePlayersHostView {

    public String inviteExpireDate() { return CliUtils.prompt("Enter invite expire date(MM/dd/yyyy): "); }

    public int askToAddPlayer() {
        String label = "Do you want to add a player/team to the tournament?";
        String choice1 = "Yes";
        String choice2 = "No";
        return CliUtils.multipleChoiceInt(label, choice1, choice2);
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

    public int addMessage() {
        return CliUtils.multipleChoiceInt("Do you want to add a message to the invite?", "Yes", "No");
    }

    public String getMessage() {
        return CliUtils.prompt("Enter message: ");
    }

    public String getEmail() { return CliUtils.prompt("Enter email: "); }

    public int askToSendEmail() {
        return CliUtils.multipleChoiceInt("Do you want to send an email?", "Yes", "No");
    }

    public int askToAddTeammate() {return CliUtils.multipleChoiceInt("Do you want to invite a teammate?", "Yes", "No");}

}
