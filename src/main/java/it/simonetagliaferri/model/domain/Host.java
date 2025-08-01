package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.observer.Subscriber;

import java.util.*;

public class Host extends User implements Subscriber {
    private List<Club> clubs;

    private Map<Tournament, List<Player>> newPlayers = new HashMap<>();

    @Override
    public void update(Club club, Tournament tournament) {
        // In this case, notify about the *latest player* joining
        Player last = tournament.getParticipants()
                .get(tournament.getParticipants().size() - 1);

        newPlayers.computeIfAbsent(tournament, t -> new ArrayList<>()).add(last);
    }

    public List<Player> getNewPlayers(Tournament t) {
        return newPlayers.getOrDefault(t, List.of());
    }

    public void clearNotifications(Tournament t) {
        newPlayers.getOrDefault(t, new ArrayList<>()).clear();
    }

    public Host(String username, String email, Role role) {
        super(username, email, role);
    }
    public Host(String username, String email) {
        super(username, email);
    }

    public void setClubs(List<Club> clubs) {
        this.clubs = Objects.requireNonNullElseGet(clubs, ArrayList::new);
    }

    public boolean hasClubs() {
        return clubs != null && !clubs.isEmpty();
    }

    public boolean addClub(Club club) {
        if (clubs == null) { clubs = new ArrayList<>(); }
        if (clubAlreadyExists(club)) { return false; }
        clubs.add(club);
        return true;
    }

    public Club getClub(Club club) {
        for (Club cl : clubs) {
            if (cl.equals(club)) { return cl; }
        }
        return null;
    }

    public List<Club> getClubs() {
        return clubs;
    }

    private boolean clubAlreadyExists(Club club) {
        return clubs.contains(club);
    }
}
