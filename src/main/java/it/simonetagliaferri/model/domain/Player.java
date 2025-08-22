package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.observer.Subscriber;

import java.util.*;

public class Player extends User implements Subscriber {

    private List<Invite> invites;
    private Map<Club, List<Tournament>> notifications;

    public Player(String username, String email, Role role) {
        super(username, email, role);
    }

    public Player(String username, String email) {
        super(username, email);
    }
    public Player(String username) {
        super(username);
    }

    @Override
    public void update(Club club, Tournament newTournament) {
        if (notifications == null) {
            notifications = new HashMap<>();
        }
        notifications.computeIfAbsent(club, k -> new ArrayList<>()).add(newTournament);
    }

    public Map<Club, List<Tournament>> getNotifications() {
        if (notifications == null) {
            return Collections.emptyMap();
        }
        return notifications;
    }

    public void setNotifications(Map<Club, List<Tournament>> notifications) {
        this.notifications = notifications;
    }

    public void clearNotifications() {
        if (notifications != null) {
            for (Club club : notifications.keySet()) {
                clearNotificationsForClub(club);
            }
        }
    }

    public void clearNotificationsForClub(Club club) {
        List<Tournament> list = notifications.get(club);
        if (list != null) {
            list.clear(); // Keep the key, just empty the list, so that the club is still a favourite
        }
    }

    public List<Invite> getInvites() {
        return invites;
    }

    public void setInvites(List<Invite> invites) {
        this.invites = invites;
    }

    public void addInvite(Invite invite) {
        if (invites == null) {
            invites = new ArrayList<>();
        }
        invites.add(invite);
    }

    public Invite getInviteForTournament(Tournament tournament) {
        for (Invite invite : invites) {
            if (invite.getTournament().isSameAs(tournament)) {
                invite.updateTournament(tournament);
                return invite;
            }
        }
        return null;
    }

    public void clearInvite(Invite invite) {
        invites.removeIf(inv -> inv.isSameAs(invite));
    }

}
