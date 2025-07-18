package it.simonetagliaferri.model.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Host extends User{
    private List<Tournament> tournaments;
    private List<Club> clubs;

    public Host(String username, String email, Role role) {
        super(username, email, role);
    }
    public Host(String username, String email) {
        super(username, email);
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

    public void setClubs(List<Club> clubs) {
        this.clubs = Objects.requireNonNullElseGet(clubs, ArrayList::new);
    }

    public boolean hasClubs() {
        return clubs != null && !clubs.isEmpty();
    }
}
