package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.exception.InvalidUserState;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;

import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.HostMapper;

public class HostDashboardLogicController extends LogicController {

    HostDAO hostDAO;

    public HostDashboardLogicController(SessionManager sessionManager, HostDAO hostDAO) {
        super(sessionManager);
        this.hostDAO = hostDAO;
    }

    public boolean additionalInfoNeeded() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        return !host.hasClub();
    }

    public HostBean getHostBean() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        return HostMapper.toBean(host);
    }

    public ClubBean getClub()  {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        if (host.hasClub()) {
            return ClubMapper.toBean(host.getClub());
        }
        else throw new InvalidUserState("Host with non-existent club should not be able to get to the dashboard.");
    }

}
