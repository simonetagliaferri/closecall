package it.simonetagliaferri.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Host extends User{
    private List<Tournament> tournaments;

    public Host(String username, String email, Role role) {
        super(username, email, role);
    }
    public Host(String username, String email) {
        super(username, email);
    }

    public static Host fromUser(User user) {
        return new Host(user.getUsername(), user.getEmail(), user.getRole());
    }

    public void addTournament(Tournament tournament) {
        tournaments.add(tournament);
    }

    public List<Tournament> getTournaments() {
        return tournaments;
    }

    public void setTournaments(List<Tournament> tournaments) {
        this.tournaments = Objects.requireNonNullElseGet(tournaments, ArrayList::new);
    }
}
