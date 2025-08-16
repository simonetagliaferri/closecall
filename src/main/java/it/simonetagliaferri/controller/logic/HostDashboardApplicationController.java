package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;

import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.HostMapper;

public class HostDashboardApplicationController extends ApplicationController {

    HostDAO hostDAO;
    ClubDAO clubDAO;

    public HostDashboardApplicationController(SessionManager sessionManager, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    private Host loadHost() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        if (host.hasClub()) { return host; }
        Club club = clubDAO.getClubByHostName(host.getUsername());
        if (club == null) {
            return host;
        }
        host.addClub(club);
        return host;
    }

    public boolean additionalInfoNeeded() {
        Host host = loadHost();
        return !host.hasClub();
    }

    public HostBean getHostBean() {
        Host host = loadHost();
        return HostMapper.toBean(host);
    }

    public ClubBean getClubBean()  {
        Host host = loadHost();
        if (host.hasClub()) {
            return ClubMapper.toBean(host.getClub());
        }
        return null;
    }

}
