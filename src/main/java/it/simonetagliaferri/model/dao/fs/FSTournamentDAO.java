package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class FSTournamentDAO extends FSDAO implements TournamentDAO {

    private final List<Tournament> tournaments;

    public FSTournamentDAO() {
        super("tournaments.json");
        tournaments = new ArrayList<>();
        loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        if (!file.exists()) return;

        try {
            List<Tournament> loaded = mapper.readValue(
                    file,
                    mapper.getTypeFactory().constructCollectionType(List.class, Tournament.class)
            );

            if (loaded != null) {
                tournaments.clear();
                tournaments.addAll(loaded);
            }

            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error loading tournaments: " + e.getMessage());
        }
    }

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        reloadIfChanged();
        tournaments.add(tournament);
        saveTournaments();
    }

    private void saveTournaments() {
        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tournaments);
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error saving tournaments: " + e.getMessage());
        }
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        reloadIfChanged();
        return getTournamentsByClub().get(club);
    }

    public Map<Club, List<Tournament>> getTournamentsByClub() {
        Map<Club, List<Tournament>> map = new HashMap<>();
        for (Tournament t : tournaments) {
            map.computeIfAbsent(t.getClub(), c -> new ArrayList<>()).add(t);
        }
        return map;
    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        reloadIfChanged();
        if (tournaments != null) {
            for (Tournament tournament : tournaments) {
                if (tournament.getClub().equals(club) && tournament.getName().equals(name) && tournament.getTournamentType().equals(tournamentType)
                        && tournament.getStartDate().equals(startDate) && tournament.getTournamentFormat().equals(tournamentFormat)) {
                    return tournament;
                }
            }
        }
        return null;
    }

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        return tournaments.stream()
                .filter(t -> t.getClub() != null && city.equalsIgnoreCase(t.getClub().getCity()))
                .collect(Collectors.toList());
    }

    private Map<String, List<Tournament>> getTournamentsByCity() {
        Map<String , List<Tournament>> map = new HashMap<>();
        for (Tournament t : tournaments) {
            map.computeIfAbsent(t.getClub().getCity(), c -> new ArrayList<>()).add(t);
        }
        return map;
    }

    @Override
    public boolean tournamentAlreadyExists(Club club, Tournament tournament) {
        return getTournament(club, tournament.getName(), tournament.getTournamentFormat(), tournament.getTournamentType(), tournament.getStartDate()) != null;
    }
}
