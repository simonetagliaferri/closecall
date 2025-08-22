package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.exception.ClubAlreadyAddedException;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.utils.converters.ClubMapper;

public class AddClubApplicationController extends ApplicationController {

    private final ClubDAO clubDAO;
    private final HostDAO hostDAO;

    public AddClubApplicationController(SessionManager sessionManager, ClubDAO clubDAO, HostDAO hostDAO) {
        super(sessionManager);
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;
    }

    public boolean addClub(ClubBean clubBean) {
        Host host = loadHost();
        Club club = ClubMapper.fromBean(clubBean);
        host.addClub(club);
        clubDAO.saveClub(club);
        return true;
    }

    private Host loadHost() {
        String username = getCurrentUserUsername();
        Host host = hostDAO.getHostByUsername(username);
        if (clubDAO.getClubByHostName(host.getUsername()) != null) {
            throw new ClubAlreadyAddedException("The host already has a club. Something went wrong during login.");
        }
        return host;
    }

}
