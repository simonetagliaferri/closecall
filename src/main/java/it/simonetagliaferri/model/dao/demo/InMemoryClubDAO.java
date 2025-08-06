package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryClubDAO implements ClubDAO {

    Map<String, Club> clubs;

    InMemoryClubDAO(Map<String, Club> clubs) {
        this.clubs = clubs;
    }

    @Override
    public Club getClubByHostName(String hostName) {
        return clubs.get(hostName);
    }

    @Override
    public void saveClub(Club club) {

    }


    public List<Club> getClubsByCity(String city) {
        List<Club> result = new ArrayList<>();
        return result;
    }

    @Override
    public Club getClubByName(String hostName, String clubName) {
        return null;
    }


}
