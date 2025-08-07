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

    private final ClubDAO clubDAO;
    private final HostDAO hostDAO;

    public AddClubLogicController(SessionManager sessionManager, ClubDAO clubDAO, HostDAO hostDAO) {
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
        User user = getCurrentUser();
        return hostDAO.getHostByUsername(user.getUsername());
    }

}
