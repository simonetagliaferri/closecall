package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryClubDAO implements ClubDAO {

    Map<String, List<Club>> clubs;

    InMemoryClubDAO(Map<String, List<Club>> clubs) {
        this.clubs = clubs;
    }

    @Override
    public List<Club> getClubs(Host host) {
        return clubs.get(host.getUsername());
    }

    @Override
    public void saveClub(Club club) {
        List<Club> clubs = getClubs(club.getHost());
        if (clubs == null) {
            clubs = new ArrayList<>();
        }
        clubs.add(club);
        this.clubs.put(club.getHost().getUsername(), clubs);
    }

    public List<Club> getClubsByCity(String city) {
        List<Club> result = new ArrayList<>();
        for (List<Club> clubs : this.clubs.values()) {
            for (Club club : clubs) {
                if (club.getCity().equals(city)) {
                    result.add(club);
                }
            }
        }
        return result;
    }

    @Override
    public Club getClubByName(Host host, String name) {
        List<Club> clubs = getClubs(host);
        for (Club club : clubs) {
            if (club.getName().equals(name)) {
                return club;
            }
        }
        return null;
    }

    @Override
    public boolean clubAlreadyExists(Club club) {
        Host host = club.getHost();
        List<Club> clubs = getClubs(host);
        if (clubs != null) {
            for (Club club1 : clubs) {
                if (club1.getName().equals(club.getName()) && club1.getCity().equals(club.getCity()) && club1.getStreet().equals(club.getStreet()) &&
                        club1.getNumber().equals(club.getNumber())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void updateClub(Club club) {
        saveClub(club);
    }

}
