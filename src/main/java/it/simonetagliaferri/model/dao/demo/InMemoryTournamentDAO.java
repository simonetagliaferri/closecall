package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
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
    public void saveTournament(Club club, Tournament tournament) {
        List<Tournament> tournaments = this.tournaments.computeIfAbsent(club.getOwner().getUsername(), k -> new ArrayList<>());
        int index = tournaments.indexOf(tournament);
        if (index >= 0) {
            tournaments.set(index, tournament);
        } else {
            tournaments.add(tournament);
        }
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        return tournaments.get(club.getOwner().getUsername());
    }

    @Override
    public Tournament getTournament(Club club, String name) {
        List<Tournament> tournamentList = tournaments.get(club.getOwner().getUsername());
        if (tournamentList != null) {
            for (Tournament tournament : tournamentList) {
                if (tournament.getName().equals(name)) {
                    return tournament;
                }
            }
        }
        return null;
    }

    public List<Tournament> getTournamentsByCity(String city) {
        List<Tournament> tournamentList = new ArrayList<>();
        String location;
        for (Map.Entry<String, List<Tournament>> entry : tournaments.entrySet()) {
            for (Tournament tournament : entry.getValue()) {
                location = tournament.getClub().getCity();
                if (location.equals(city)) {
                    tournamentList.add(tournament);
                }
            }
        }
        return tournamentList;
    }

    @Override
    public boolean tournamentAlreadyExists(Club club, Tournament tournament) {
        return getTournament(club, tournament.getName()) != null;
    }
}
