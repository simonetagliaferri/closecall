package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.utils.CliUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvitePlayersPlayerView {

    public int listNotifications(List<InviteBean> invites) {
        List<String> inv = new ArrayList<>();
        for (InviteBean invite : invites) {
            String inviteInfo = "Invite for tournament " + invite.getTournament().getTournamentName() +
                    " from club " + invite.getTournament().getClub().getName();
            inv.add(inviteInfo);
        }
        return CliUtils.multipleChoiceInt("Select the invite you want to handle(0 to go back): ", inv);
    }

    public void expandedInvite(InviteBean invite){
        CliUtils.println("Invite for tournament " + invite.getTournament().getTournamentName() +
                " from club " + invite.getTournament().getClub().getName());
        CliUtils.println("The invite expires on " + invite.getExpiryDate());
        CliUtils.println("Tournament details:");
        CliUtils.println("Tournament name: " + invite.getTournament().getTournamentName());
        CliUtils.println("Club name: " + invite.getTournament().getClub().getName());
        CliUtils.println("Club address: " + invite.getTournament().getClub().getAddress());
        CliUtils.println("Tournament format: " + invite.getTournament().getTournamentFormat());
        CliUtils.println("Tournament type: " + invite.getTournament().getTournamentType());
        CliUtils.println("Tournament join fee: " + invite.getTournament().getJoinFee());
        CliUtils.println("Court costs: " + invite.getTournament().getCourtPrice());
    }

    public void expiredInvite(LocalDate expiryDate) {
        CliUtils.println("This invite expired on " + expiryDate);
    }

    public InviteStatus handleInvite() {
        int choice = CliUtils.multipleChoiceInt("What do you want to do?", "Accept", "Decline", "Ignore");
        return InviteStatus.values()[choice-1];
    }

    public void teamInvite(String teammateName) {
        CliUtils.println("You got invited in a team, your team mate is: " + teammateName);
    }

    public void noNotificatons() {
        CliUtils.println("There are no notifications for you.");
    }
}
