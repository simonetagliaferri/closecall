package it.simonetagliaferri.model.dao.fs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.strategy.DoubleEliminationStrategy;
import it.simonetagliaferri.model.strategy.RoundRobinStrategy;
import it.simonetagliaferri.model.strategy.SingleEliminationStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.utils.CliUtils;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.*;

public class FSTournamentDAO implements TournamentDAO {

    private final File file = new File("tournaments.json");
    private final Map<String, List<Tournament>> tournaments = new HashMap<>();

    RuntimeTypeAdapterFactory<TournamentFormatStrategy> formatStrategyAdapter =
            RuntimeTypeAdapterFactory.of(TournamentFormatStrategy.class, "type")
                    .registerSubtype(RoundRobinStrategy.class, "RoundRobin")
                    .registerSubtype(SingleEliminationStrategy.class, "SingleElimination")
                    .registerSubtype(DoubleEliminationStrategy.class, "DoubleElimination");

    Gson gson =  new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new GsonLocalDateAdapter())
            .registerTypeAdapterFactory(formatStrategyAdapter)
            .create();
    public FSTournamentDAO() {
        loadTournaments();
    }

    private void loadTournaments() {
        if (!file.exists()) return;
        try (Reader reader = new FileReader(file)) {
            Type type = new TypeToken<Map<String, List<Tournament>>>() {
            }.getType();
            Map<String, List<Tournament>> loaded = gson.fromJson(reader, type);
            if (loaded != null) {
                tournaments.clear();
                tournaments.putAll(loaded);
            }
        } catch (IOException e) {
            CliUtils.println("Error loading users: " + e.getMessage());
        }
    }


    @Override
    public void saveTournament(Club club, Tournament tournament) {
        tournaments.computeIfAbsent(club.getName(), k -> new ArrayList<>()).add(tournament);
        saveTournaments();
    }

    private void saveTournaments() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(tournaments, writer);
        } catch (IOException e) {
            CliUtils.println("Error saving tournaments: " + e.getMessage());
        }
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        loadTournaments();
        return tournaments.get(club.getName());
    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        return null;
    }

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        return List.of();
    }

    @Override
    public boolean tournamentAlreadyExists(Club club, Tournament tournament) {
        return false;
    }
}
