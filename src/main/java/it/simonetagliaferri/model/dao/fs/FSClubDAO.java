package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

import java.util.List;

public class FSClubDAO implements ClubDAO {
    @Override
    public List<Club> getClubs(Host host) {
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
    public Club getClubByName(Host host, String name) {
        return null;
    }

    @Override
    public boolean clubAlreadyExists(Club club) {
        return false;
    }
}
