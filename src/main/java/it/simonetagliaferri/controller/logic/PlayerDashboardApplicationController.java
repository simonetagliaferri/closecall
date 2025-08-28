package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.converters.PlayerMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;

public class PlayerDashboardApplicationController extends ApplicationController {

    private final PlayerDAO playerDAO;
    private final TournamentDAO tournamentDAO;

    public PlayerDashboardApplicationController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
    }

    private Player loadPlayer() {
        String username = getCurrentUserUsername();
        return playerDAO.findByUsername(username);
    }

    private List<Tournament> loadTournaments() {
        Player player = loadPlayer();
        return tournamentDAO.getPlayerTournaments(player);
    }

    public PlayerBean getPlayerBean() {
        return PlayerMapper.toBean(loadPlayer());
    }

    public List<TournamentBean> getMyTournaments() {
        List<TournamentBean> tournaments = new ArrayList<>();
        List<Tournament> tournamentList = loadTournaments();
        for (Tournament tournament : tournamentList) {
            tournaments.add(TournamentMapper.toBean(tournament));
        }
        return tournaments;
    }
}
