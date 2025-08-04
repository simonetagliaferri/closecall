package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.domain.User;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class ManageTournamentsLogicController extends LogicController {

    TournamentDAO tournamentDAO;
    HostDAO hostDAO;
    ClubDAO clubDAO;

    public ManageTournamentsLogicController(SessionManager sessionManager, TournamentDAO tournamentDAO, HostDAO hostDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.tournamentDAO = tournamentDAO;
        this.hostDAO = hostDAO;
        this.clubDAO = clubDAO;
    }

    public List<TournamentBean> getTournaments() {
        User user = getCurrentUser();
        Host host = hostDAO.getHostByUsername(user.getUsername());
        List<Club> clubs = host.getClubs();
        List<Tournament> tournaments;
        List<TournamentBean> tournamentBeans = new ArrayList<>();
        for (Club club : clubs) {
            tournaments = club.getClubTournaments();
            if (tournaments != null && !tournaments.isEmpty()) {
                for (Tournament tournament : tournaments) {
                    tournamentBeans.add(TournamentMapper.toBean(tournament));
                }
            }
        }
        return tournamentBeans;
    }

}
