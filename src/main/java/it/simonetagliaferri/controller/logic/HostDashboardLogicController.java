package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.converters.HostMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class HostDashboardLogicController extends LogicController {
    TournamentDAO tournamentDAO;

    public HostDashboardLogicController(AppContext appContext) {
        super(appContext);
        tournamentDAO = this.appContext.getDAOFactory().getTournamentDAO();
    }

    /*
    Will have something like a getHostInfo method to get the host's proprietary attributes from a DAO.
    Also, a method to check, from the DAO, if it's the first login, if it is the required extra infos will be asked,
    just the first time, the other times the flag I suppose will be set to 1 in persistence and so nothing will be asked
     */

    public HostBean getHostBean() {
        User user = getCurrentUser();
        Host host = Host.fromUser(user);
        return HostMapper.toBean(host);
    }

    public List<TournamentBean> getTournaments() {
        User user = getCurrentUser();
        Host host = Host.fromUser(user);
        List<Tournament> tournaments = tournamentDAO.getTournaments(host);
        List<TournamentBean> tournamentBeans = new ArrayList<>();
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament tournament : tournaments) {
                tournamentBeans.add(TournamentMapper.toBean(tournament));
            }
        }
        return tournamentBeans;
    }
}
