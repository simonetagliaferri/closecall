package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.util.List;

public interface TournamentDAO {
    void saveTournament(Club club, Tournament tournament);

    List<Tournament> getTournaments(Club club);

    Tournament getTournament(Club club, String name);

    List<Tournament> getTournamentsByCity(String city);

    List<Tournament> getPlayerTournaments(Player player);
}
