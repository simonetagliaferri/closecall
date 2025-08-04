package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;

import it.simonetagliaferri.utils.converters.HostMapper;

public class HostDashboardLogicController extends LogicController {

    HostDAO hostDAO;

    public HostDashboardLogicController(SessionManager sessionManager, HostDAO hostDAO) {
        super(sessionManager);
        this.hostDAO = hostDAO;
    }

    public boolean additionalInfoNeeded() {
        Host host = hostDAO.getHostByUsername(getCurrentUser().getUsername());
        return !host.hasClubs();
    }

    public HostBean getHostBean() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        return HostMapper.toBean(host);
    }

}
