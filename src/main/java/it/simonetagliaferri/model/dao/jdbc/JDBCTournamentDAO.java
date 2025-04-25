package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.List;

public class JDBCTournamentDAO implements TournamentDAO {

    @Override
    public void addTournament(Host host, Tournament tournament) {

    }

    @Override
    public List<Tournament> getTournaments(Host host) {
        return null;
    }

    @Override
    public void updateTournament(Host host, Tournament tournament) {

    }
}
