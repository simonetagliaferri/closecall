package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;

import java.util.List;

public class FSClubDAO implements ClubDAO {
    @Override
    public List<Club> getClubs(String hostName) {
        return List.of();
    }

    @Override
    public void saveClub(Club club) {

    }

    @Override
    public List<Club> getClubsByCity(String city) {
        return List.of();
    }

    @Override
    public Club getClubByName(String hostName, String clubName) {
        return null;
    }

    @Override
    public boolean clubAlreadyExists(Club club) {
        return false;
    }

}
