package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.*;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class PlayerDashboardLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    ClubDAO clubDAO;

    public PlayerDashboardLogicController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO, ClubDAO clubDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
        this.clubDAO = clubDAO;
    }

    public List<TournamentBean> getMyTournaments() {
        User user = getCurrentUser();
        Player player = playerDAO.findByUsername(user.getUsername());
        List<TournamentBean> tournaments = new ArrayList<>();
        Tournament tournament;
        for (Team team : player.getTeams()) {
            tournament = team.getTournament();
            tournaments.add(TournamentMapper.toBean(tournament));
        }
        return tournaments;
    }
}
