package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.dao.demo.InMemoryTournamentDAO;
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
        return InMemoryTournamentDAO.getTournament(club, name, tournaments);
    }

    public List<Tournament> getTournamentsByCity(String city) {
        return InMemoryTournamentDAO.getTournaments(city, tournaments);
    }

    @Override
    public List<Tournament> getPlayerTournaments(Player player) {
        return InMemoryTournamentDAO.getPlayerTournaments(player, tournaments);
    }

}
