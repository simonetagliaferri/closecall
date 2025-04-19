package it.simonetagliaferri.model.domain;

import java.util.List;

public class Host extends User{
    private List<Tournament> tournaments;

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
        this.tournaments = tournaments;
    }
}
