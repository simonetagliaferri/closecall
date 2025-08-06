package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Club;

import java.util.List;

public interface ClubDAO {

    Club getClubByHostName(String hostName);
    void saveClub(Club club);
    List<Club> getClubsByCity(String city);
    Club getClubByName(String hostName, String clubName);
}
