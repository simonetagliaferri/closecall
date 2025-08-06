package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.converters.ClubMapper;

public class AddClubLogicController extends LogicController {

    ClubDAO clubDAO;
    HostDAO hostDAO;

    public AddClubLogicController(SessionManager sessionManager, ClubDAO clubDAO, HostDAO hostDAO) {
        super(sessionManager);
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;

    }

    public boolean addClub(ClubBean clubBean) {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        Club club = ClubMapper.fromBean(clubBean);
        if (!host.addClub(club)) {
            return false;
        }
        clubDAO.saveClub(club);
        hostDAO.saveHost(host);
        return true;
    }

    public boolean firstClub() {
        User currentUser = sessionManager.getCurrentUser();
        Host host = hostDAO.getHostByUsername(currentUser.getUsername());
        return !host.hasClub();
    }

}
