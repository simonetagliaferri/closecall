package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

import java.util.List;

public class JDBCClubDAO implements ClubDAO {
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

    @Override
    public void updateClub(Club club) {

    }

}
