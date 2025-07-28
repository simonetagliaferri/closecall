package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.converters.ClubMapper;
import it.simonetagliaferri.utils.converters.HostMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class HostDashboardLogicController extends LogicController {
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;

    public HostDashboardLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    /*
    Will have something like a getHostInfo method to get the host's proprietary attributes from a DAO.
    Also, a method to check, from the DAO, if it's the first login, if it is the required extra infos will be asked,
    just the first time, the other times the flag I suppose will be set to 1 in persistence and so nothing will be asked
     */

    public boolean additionalInfoNeeded() {
        Host host = hostDAO.getHostByUsername(getCurrentUser().getUsername());
        return clubDAO.getClubs(host).isEmpty();
    }

    public HostBean getHostBean() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        return HostMapper.toBean(host);
    }

    public List<TournamentBean> getTournaments() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        List<Club> clubs = clubDAO.getClubs(host);
        List<Tournament> tournaments;
        List<TournamentBean> tournamentBeans = new ArrayList<>();
        for (Club club : clubs) {
            tournaments = tournamentDAO.getTournaments(club);
            if (tournaments != null && !tournaments.isEmpty()) {
                for (Tournament tournament : tournaments) {
                    tournamentBeans.add(TournamentMapper.toBean(tournament));
                }
            }
        }
        return tournamentBeans;
    }

    public List<ClubBean> getClubs() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        List<Club> clubs = clubDAO.getClubs(host);
        List<ClubBean> clubBeans = new ArrayList<>();
        if (clubs != null && !clubs.isEmpty()) {
            for (Club club : clubs) {
                clubBeans.add(ClubMapper.toBean(club));
            }
        }
        return clubBeans;
    }
}
