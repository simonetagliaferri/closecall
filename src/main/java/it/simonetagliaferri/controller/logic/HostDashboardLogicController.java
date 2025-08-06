package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.exception.InvalidUserState;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;

import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.HostMapper;

public class HostDashboardLogicController extends LogicController {

    HostDAO hostDAO;
    ClubDAO clubDAO;

    public HostDashboardLogicController(SessionManager sessionManager, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    public boolean additionalInfoNeeded() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        if (host.hasClub()) { return false; }
        Club club = clubDAO.getClubByHostName(host.getUsername());
        if (club == null) return true;
        host.addClub(club);
        return false;
    }

    public HostBean getHostBean() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        return HostMapper.toBean(host);
    }

    public ClubBean getClub()  {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        Club club;
        if (host.hasClub()) {
            return ClubMapper.toBean(host.getClub());
        }
        else {
            club = clubDAO.getClubByHostName(host.getUsername());
            if (club == null) {
                throw new InvalidUserState("A host without a club should not be able to get to the dashboard.");
            }
            return ClubMapper.toBean(club);
        }
    }

}
