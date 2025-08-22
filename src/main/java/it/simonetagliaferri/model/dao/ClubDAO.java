package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Club;

public interface ClubDAO {

    Club getClubByHostName(String hostName);

    void saveClub(Club club);
}
