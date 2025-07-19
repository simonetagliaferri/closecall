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

public class AddClubLogicController extends LogicController {

    ClubDAO clubDAO;
    HostDAO hostDAO;

    public AddClubLogicController(SessionManager sessionManager, ClubDAO clubDAO, HostDAO hostDAO) {
        super(sessionManager);
        this.clubDAO = clubDAO;
        this.hostDAO = hostDAO;

    }

    public void addClub(ClubBean clubBean) {
        User currentUser = sessionManager.getCurrentUser();
        Host host = hostDAO.getHostByUsername(currentUser.getUsername());
        HostBean hostBean = HostMapper.toBean(host);
        clubBean.setOwner(hostBean);
        Club club = ClubMapper.fromBean(clubBean);
        clubDAO.saveClub(club);
    }
}
