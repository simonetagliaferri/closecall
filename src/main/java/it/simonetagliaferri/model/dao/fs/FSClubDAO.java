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
}
