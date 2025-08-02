package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Club;

import java.util.List;

public interface ClubDAO {

    List<Club> getClubs(String hostName);
    void saveClub(Club club);
    List<Club> getClubsByCity(String city);
    Club getClubByName(String hostName, String clubName);
    boolean clubAlreadyExists(Club club);
}
