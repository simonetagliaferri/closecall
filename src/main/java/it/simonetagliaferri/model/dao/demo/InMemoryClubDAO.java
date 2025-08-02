package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryClubDAO implements ClubDAO {

    Map<String, List<Club>> clubs;

    InMemoryClubDAO(Map<String, List<Club>> clubs) {
        this.clubs = clubs;
    }

    @Override
    public List<Club> getClubs(String hostName) {
        return clubs.get(hostName);
    }

    @Override
    public void saveClub(Club club) {
        String hostName = club.getHost().getUsername();
        List<Club> clubs = this.clubs.computeIfAbsent(hostName, k -> new ArrayList<>());
        int index = clubs.indexOf(club);
        if (index >= 0) {
            clubs.set(index, club);
        } else {
            clubs.add(club);
        }
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
    public Club getClubByName(String hostName, String clubName) {
        List<Club> clubs = getClubs(hostName);
        for (Club club : clubs) {
            if (club.getName().equals(clubName)) {
                return club;
            }
        }
        return null;
    }

    @Override
    public boolean clubAlreadyExists(Club club) {
        String hostName = club.getHost().getUsername();
        List<Club> clubs = getClubs(hostName);
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

}
