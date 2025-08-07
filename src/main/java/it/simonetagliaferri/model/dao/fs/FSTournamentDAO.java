package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

import java.io.*;
import java.util.*;

public class FSTournamentDAO implements TournamentDAO {

    private static final String outputFile = "tournaments.db";
    Map<String, List<Tournament>> tournaments;

    public FSTournamentDAO() {
        tournaments = new HashMap<>();
        loadTournaments();
    }

    @SuppressWarnings("unchecked")
    private void loadTournaments() {
        File file = new File(outputFile);
        if (!file.exists()) return; // Start empty if no file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            tournaments = (Map<String, List<Tournament>>) ois.readObject();
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error loading users", e);
        }
    }

    private void saveTournaments() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(outputFile))) {
            oos.writeObject(tournaments);
        } catch (IOException e) {
            throw new RuntimeException("Error saving users", e);
        }
    }

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        List<Tournament> tournaments = this.tournaments
                .computeIfAbsent(club.getOwner().getUsername(), k -> new ArrayList<>());

        for (int i = 0; i < tournaments.size(); i++) {
            Tournament t = tournaments.get(i);
            if (t.getName().equals(tournament.getName())) {
                tournaments.set(i, tournament);
                saveTournaments();
                return;
            }
        }

        tournaments.add(tournament);
        saveTournaments();
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        List<Tournament> clubTournaments = this.tournaments.get(club.getOwner().getUsername());
        if (clubTournaments == null) {
            return new ArrayList<>();
        }
        return clubTournaments;
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
