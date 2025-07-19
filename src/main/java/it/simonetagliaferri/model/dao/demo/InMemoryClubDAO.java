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

}
