package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.*;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.utils.CliUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class InvitePlayersPlayerView {

    public enum AddClubToFavourites {
        YES,
        NO
    }

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
        TournamentBean tournament = invite.getTournament();
        ClubBean club = tournament.getClub();
        CliUtils.println("Invite for tournament " + tournament.getTournamentName() +
                " from club " + club.getName());
        CliUtils.println("The invite expires on " + invite.getExpiryDate());
        CliUtils.println("Tournament details:");
        HostDashboardCLIView.listTournament(tournament);
        CliUtils.println("Club details:");
        CliUtils.println("Club name: " + club.getName());
        CliUtils.println("Club street: " + club.getStreet());
        CliUtils.println("Club number: " + club.getNumber());
        CliUtils.println("Club city: " + club.getCity());
        CliUtils.println("Club state: " + club.getState());
        CliUtils.println("Club zip: " + club.getZip());
        CliUtils.println("Club country: " + club.getCountry());
        CliUtils.println("Club phone: " + club.getPhone());

    }

    public void expiredInvite(LocalDate expiryDate) {
        CliUtils.println("This invite expired on " + expiryDate);
    }

    public InviteStatus handleInvite() {
        int choice = CliUtils.multipleChoiceInt("What do you want to do?", "Accept", "Decline", "Ignore");
        return InviteStatus.values()[choice-1];
    }

    public void noInvites() {
        CliUtils.println("There are no notifications for you.");
    }

    public AddClubToFavourites addClubToFavourites() {
        int choice = CliUtils.multipleChoiceInt("Do you want to add this club to your favourites?", "Yes", "No");
        return AddClubToFavourites.values()[choice-1];
    }

}
