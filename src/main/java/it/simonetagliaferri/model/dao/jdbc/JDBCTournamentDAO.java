package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;
import java.util.List;

public class JDBCTournamentDAO implements TournamentDAO {

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        //TODO
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        //TODO
        return null;
    }

    @Override
    public Tournament getTournament(Club club, String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        //TODO
        return null;
    }

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        //TODO
        return null;
    }

    @Override
    public boolean tournamentAlreadyExists(Club club, Tournament tournament) {
        //TODO
        return false;
    }
}
