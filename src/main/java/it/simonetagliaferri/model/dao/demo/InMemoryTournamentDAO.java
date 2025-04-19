package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTournamentDAO implements TournamentDAO {
    Map<Pair<Integer, String>, Tournament> tournaments;
    public InMemoryTournamentDAO(Map<Pair<Integer, String>, Tournament> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public void addTournament(Host host, Tournament tournament) {
        int tournamentID = host.getTournaments().size();
        Pair<Integer, String> p = new Pair<>(tournamentID, host.getUsername());
        System.out.println("Adding tournament " + tournament.getTournamentName() + " with id " + tournamentID);
        tournaments.put(p, tournament);
    }

    @Override
    public List<Tournament> getTournaments(String hostUsername) {
        List<Tournament> hostedTournaments = new ArrayList<>();
        for (Tournament tournament : tournaments.values()) {
            if (tournament.getHostUsername().equals(hostUsername)) {
                hostedTournaments.add(tournament);
            }
        }
        return hostedTournaments;
    }
}
