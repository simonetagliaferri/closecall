package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.observer.Subscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Player extends User implements Subscriber {

    public Player(String username, String email, Role role) {
        super(username, email, role);
    }
    public Player(String username, String email) {
        super(username, email);
    }
    public Player(String username) {
        super(username);
    }

    private List<Invite> invites;

    private List<Club> favouriteClubs;
    private List<Team> teams;

    private Map<Club, List<Tournament>> notifications;

    @Override
    public void update(Club club, Tournament newTournament) {
        if (favouriteClubs == null) { favouriteClubs = new ArrayList<>(); }
        if (notifications == null) { notifications = new HashMap<>(); }
        notifications.computeIfAbsent(club, k -> new ArrayList<>()).add(newTournament);
    }

    public List<Tournament> getNotificationsForClub(Club club) {
        return notifications.getOrDefault(club, List.of());
    }

    public Map<Club, List<Tournament>> getNotifications() {
        return notifications;
    }

    public void addTeam(Team team) {
        if (teams == null) { teams = new ArrayList<>(); }
        teams.add(team);
    }

    public void removeTeam(Team team) {
        if (teams == null) { return; }
        teams.remove(team);
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void clearNotifications() {
        for (Club club : notifications.keySet()) {
            clearNotificationsForClub(club);
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

    public void addInvite(Invite invite) {
        if (invites == null) { invites = new ArrayList<>(); }
        invites.add(invite);
    }

    public Invite getInviteForTournament(Tournament tournament) {
        for (Invite invite : invites) {
            if (sameTournament(invite.getTournament(), tournament)) {
                return invite;
            }
        }
        return null;
    }

    public void clearInvite(Invite invite) {
        invites.remove(invite);
    }

    public Invite isInvitedToTournament(Tournament tournament) {
        if (tournament == null) { return null; }
        for (Invite invite : invites) {
            if (sameTournament(invite.getTournament(), tournament)) {
                return invite;
            }
        }
        return null;
    }

    private boolean sameTournament(Tournament tournament1, Tournament tournament2) {
        Club club = tournament1.getClub();
        Club cl = tournament2.getClub();
        return tournament1.getName().equals(tournament2.getName()) && club.getName().equals(cl.getName()) &&
                club.getOwner().getUsername().equals(cl.getOwner().getUsername());
    }

}
