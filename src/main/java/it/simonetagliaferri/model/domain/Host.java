package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.observer.Subscriber;

import java.util.*;

public class Host extends User implements Subscriber {

    private Club club;

    private Map<Tournament, List<Player>> newPlayers;

    public Host(String username, String email, Role role) {
        super(username, email, role);
    }

    public Host(String username, String email) {
        super(username, email);
    }

    public Host(String username) {
        super(username);
    }

    public Host(String username, String password, String email, Role role) {
        super(username, password, email, role);
    }

    @Override
    public void update(Club club, Tournament tournament) {
        Player last = tournament.getParticipants()
                .get(tournament.getParticipants().size() - 1);
        if (newPlayers == null) {
            newPlayers = new HashMap<>();
        }
        newPlayers.computeIfAbsent(tournament, t -> new ArrayList<>()).add(last);
    }

    public void clearNotifications() {
        if (newPlayers != null) {
            newPlayers.clear();
        }
    }

    public Map<Tournament, List<Player>> getNewPlayers() {
        if (newPlayers == null) {
            return Collections.emptyMap();
        }
        return newPlayers;
    }

    public void setNewPlayers(Map<Tournament, List<Player>> newPlayers) {
        this.newPlayers = newPlayers;
    }

    public boolean hasClub() {
        return club != null;
    }

    public void addClub(Club club) {
        club.setOwner(this);
        this.club = club;
    }


    public Club getClub() {
        return club;
    }
}
