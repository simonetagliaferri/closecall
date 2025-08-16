package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class FSTournamentDAO extends FSDAO implements TournamentDAO {

    private static final String FILE_NAME = "tournaments.db";
    private final Map<String, List<Tournament>> tournaments;

    public FSTournamentDAO(Path baseDir) {
        super(baseDir, FILE_NAME);
        tournaments = new HashMap<>();
        loadTournaments();
    }

    @SuppressWarnings("unchecked")
    private void loadTournaments() {
        Path f = file();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<String, List<Tournament>> loaded = (Map<String, List<Tournament>>) obj;
                tournaments.clear();
                tournaments.putAll(loaded);
            }
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error loading tournaments", e);
        }
    }

    private void saveTournaments() {
        Path f = file();
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(f, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(tournaments);
        } catch (IOException e) {
            throw new DAOException("Error saving tournaments", e);
        }
    }

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        List<Tournament> clubTournaments = this.tournaments
                .computeIfAbsent(club.getOwner().getUsername(), k -> new ArrayList<>());

        for (int i = 0; i < clubTournaments.size(); i++) {
            Tournament t = clubTournaments.get(i);
            if (t.getName().equals(tournament.getName())) {
                clubTournaments.set(i, tournament);
                saveTournaments();
                return;
            }
        }

        clubTournaments.add(tournament);
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
                if (location.equalsIgnoreCase(city)) {
                    tournamentList.add(tournament);
                }
            }
        }
        return tournamentList;
    }

    @Override
    public List<Tournament> getPlayerTournaments(Player player) {
        List<Tournament> tournamentList = new ArrayList<>();
        for (Map.Entry<String, List<Tournament>> entry : tournaments.entrySet()) {
            for (Tournament tournament : entry.getValue()) {
                if (tournament.playerAlreadyConfirmed(player)) {
                    tournamentList.add(tournament);
                }
            }
        }
        return tournamentList;
    }

}
