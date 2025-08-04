package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class FSTournamentDAO extends FSDAO implements TournamentDAO {

    private final Map<String, List<Tournament>> tournaments = new HashMap<>();

    public FSTournamentDAO() {
        super("tournaments.json");
        loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        if (!file.exists()) return;

        try {
            // Deserialize the map of hosts from JSON
            Map<String, List<Tournament>> loaded = mapper.readValue(file,
                    mapper.getTypeFactory().constructMapType(HashMap.class, String.class, Tournament.class));

            if (loaded != null) {
                tournaments.clear();
                tournaments.putAll(loaded);
            }

            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error loading hosts: " + e.getMessage());
        }

    }

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        reloadIfChanged();
        tournaments.computeIfAbsent(club.getName(), k -> new ArrayList<>()).add(tournament);
        saveTournaments();
    }

    private void saveTournaments() {
        try {
            // Serialize the map of hosts back to JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, tournaments);
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error saving hosts: " + e.getMessage());
        }
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        reloadIfChanged();
        return tournaments.get(club.getName());
    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        reloadIfChanged();
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

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        reloadIfChanged();
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
        return getTournament(club, tournament.getTournamentName(), tournament.getTournamentFormat(), tournament.getTournamentType(), tournament.getStartDate()) != null;
    }
}
