package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.List;

public interface TournamentDAO {
    void addTournament(Host host, Tournament tournament);
    List<Tournament> getTournaments(String hostUsername);
}
