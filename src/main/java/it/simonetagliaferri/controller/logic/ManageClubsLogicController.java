package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.converters.ClubMapper;

import java.util.ArrayList;
import java.util.List;

public class ManageClubsLogicController extends LogicController {

    HostDAO hostDAO;
    ClubDAO clubDAO;

    public ManageClubsLogicController(SessionManager sessionManager, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    public List<ClubBean> getClubs() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        List<Club> clubs = clubDAO.getClubs(host.getUsername());
        List<ClubBean> clubBeans = new ArrayList<>();
        if (clubs != null && !clubs.isEmpty()) {
            for (Club club : clubs) {
                clubBeans.add(ClubMapper.toBean(club));
            }
        }
        return clubBeans;
    }
}
