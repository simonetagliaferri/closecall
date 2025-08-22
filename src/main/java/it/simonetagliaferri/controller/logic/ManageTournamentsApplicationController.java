package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class ManageTournamentsApplicationController extends ApplicationController {

    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;

    public ManageTournamentsApplicationController(SessionManager sessionManager, TournamentDAO tournamentDAO, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    private List<Tournament> loadTournaments() {
        String username = getCurrentUserUsername();
        Host host = hostDAO.getHostByUsername(username);
        Club club = clubDAO.getClubByHostName(host.getUsername());
        return tournamentDAO.getTournaments(club);
    }

    public List<TournamentBean> getTournaments() {
        List<Tournament> tournaments = loadTournaments();
        List<TournamentBean> tournamentBeans = new ArrayList<>();
        if (tournaments != null && !tournaments.isEmpty()) {
            for (Tournament tournament : tournaments) {
                tournamentBeans.add(TournamentMapper.toBean(tournament));
            }
        }
        return tournamentBeans;
    }

}
