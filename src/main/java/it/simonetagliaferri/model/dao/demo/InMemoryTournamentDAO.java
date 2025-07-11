package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class InMemoryTournamentDAO implements TournamentDAO {
    Map<String, List<Tournament>> tournaments;
    public InMemoryTournamentDAO(Map<String, List<Tournament>> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public void addTournament(Host host, Tournament tournament) {
        tournament.setId(UUID.randomUUID().toString()); // UUID used so that there is no need to check for collisions.
        tournaments.computeIfAbsent(host.getUsername(), k -> new ArrayList<>()).add(tournament);
    }

    @Override
    public List<Tournament> getTournaments(Host host) {
        return tournaments.get(host.getUsername());
    }

    @Override
    public void updateTournament(Host host, Tournament tournament) {
        String hostUsername = host.getUsername();
        List<Tournament> tournamentList = tournaments.get(hostUsername);
        if (tournamentList != null) {
            for (int i = 0; i < tournamentList.size(); i++) {
                if (tournamentList.get(i).getId().equals(tournament.getId())) {
                    tournamentList.set(i, tournament);
                    return;
                }
            }
        }
    }

    @Override
    public Tournament getTournament(Host host, String id) {
        List<Tournament> tournamentList = tournaments.get(host.getUsername());
        if (tournamentList != null) {
            for (Tournament tournament : tournamentList) {
                if (tournament.getId().equals(id)) {
                    return tournament;
                }
            }
        }
        return null;
    }
}
