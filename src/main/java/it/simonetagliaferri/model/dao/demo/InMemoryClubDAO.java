package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

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

}
