package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTournamentDAO implements TournamentDAO {
    Map<Club, List<Tournament>> tournaments;
    public InMemoryTournamentDAO(Map<Club, List<Tournament>> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        List<Tournament> tournaments = this.tournaments.computeIfAbsent(club, k -> new ArrayList<>());
        int index = tournaments.indexOf(tournament);
        if (index >= 0) {
            tournaments.set(index, tournament);
        } else {
            tournaments.add(tournament);
        }
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        return tournaments.get(club);
    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        List<Tournament> tournamentList = tournaments.get(club);
        if (tournamentList != null) {
            for (Tournament tournament : tournamentList) {
                if (tournament.getName().equals(name) && tournament.getTournamentType().equals(tournamentType)
                && tournament.getStartDate().equals(startDate) && tournament.getTournamentFormat().equals(tournamentFormat)) {
                    return tournament;
                }
            }
        }
        return null;
    }

    public List<Tournament> getTournamentsByCity(String city) {
        List<Tournament> tournamentList = new ArrayList<>();
        String location;
        for (Map.Entry<Club, List<Tournament>> entry : tournaments.entrySet()) {
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
        return getTournament(club, tournament.getName(), tournament.getTournamentFormat(), tournament.getTournamentType(), tournament.getStartDate()) != null;
    }
}
