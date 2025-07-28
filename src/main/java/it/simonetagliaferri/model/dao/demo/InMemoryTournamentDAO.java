package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryTournamentDAO implements TournamentDAO {
    Map<String, List<Tournament>> tournaments;
    public InMemoryTournamentDAO(Map<String, List<Tournament>> tournaments) {
        this.tournaments = tournaments;
    }

    @Override
    public void addTournament(Club club, Tournament tournament) {
        tournaments.computeIfAbsent(club.getName(), k -> new ArrayList<>()).add(tournament);
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        return tournaments.get(club.getName());
    }

    @Override
    public void updateTournament(Club club, Tournament tournament) {
        String clubName = club.getName();
        List<Tournament> tournamentList = tournaments.get(clubName);
        if (tournamentList != null) {
            for (int i = 0; i < tournamentList.size(); i++) {
                if (tournamentList.get(i).equals(tournament)) {
                    tournamentList.set(i, tournament);
                    return;
                }
            }
        }
    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        List<Tournament> tournamentList = tournaments.get(club.getName());
        if (tournamentList != null) {
            for (Tournament tournament : tournamentList) {
                if (tournament.getTournamentName().equals(name) && tournament.getTournamentType().equals(tournamentType)
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
}
