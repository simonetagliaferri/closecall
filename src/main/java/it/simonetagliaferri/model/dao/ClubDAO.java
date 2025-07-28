package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

import java.util.List;

public interface ClubDAO {

    List<Club> getClubs(Host host);
    void saveClub(Club club);
    List<Club> getClubsByCity(String city);
    Club getClubByName(Host host, String name);
}
