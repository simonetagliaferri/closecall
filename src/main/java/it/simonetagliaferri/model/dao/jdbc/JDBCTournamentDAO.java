package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCTournamentDAO implements TournamentDAO {

    @Override
    public void addTournament(Club club, Tournament tournament) {

    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        return new ArrayList<>();
    }

    @Override
    public void updateTournament(Club club, Tournament tournament) {

    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        return null;
    }

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        return List.of();
    }
}
