package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTournamentDAO implements TournamentDAO {
    Map<String, List<Tournament>> tournaments;
    public InMemoryTournamentDAO(Map<String, List<Tournament>> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public void addTournament(Host host, Tournament tournament) {
        String hostUsername = host.getUsername();
        List<Tournament> tournamentList = tournaments.get(hostUsername);
        if (tournamentList == null) {
            tournamentList = new ArrayList<>();
        }
        tournamentList.add(tournament);
        tournaments.put(hostUsername, tournamentList);
    }

    @Override
    public List<Tournament> getTournaments(Host host) {
        return tournaments.get(host.getUsername());
    }

    @Override
    public void updateTournament(Host host, Tournament tournament) {
        String hostUsername = host.getUsername();
        List<Tournament> tournamentList = tournaments.get(hostUsername);
        tournamentList.remove(tournament);
        tournamentList.add(tournament);
    }
}
