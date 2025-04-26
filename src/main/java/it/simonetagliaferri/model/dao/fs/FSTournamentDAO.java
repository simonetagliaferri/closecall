package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.ArrayList;
import java.util.List;

public class FSTournamentDAO implements TournamentDAO {

    @Override
    public void addTournament(Host host, Tournament tournament) {

    }

    @Override
    public List<Tournament> getTournaments(Host host) {
        return new ArrayList<>();
    }

    @Override
    public void updateTournament(Host host, Tournament tournament) {

    }
}
