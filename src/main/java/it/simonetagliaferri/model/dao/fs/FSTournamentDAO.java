package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import java.time.LocalDate;
import java.util.*;

public class FSTournamentDAO implements TournamentDAO {

    @Override
    public void saveTournament(Club club, Tournament tournament) {

    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        return List.of();
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
