package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HandleNotificationsLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;

    public HandleNotificationsLogicController(SessionManager sessionManager, PlayerDAO playerDAO, TournamentDAO tournamentDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.tournamentDAO = tournamentDAO;
    }

    public List<TournamentBean> getPlayerNotifications() {
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
        Map<Club, List<Tournament>> notifications = player.getNotifications();
        List<TournamentBean> tournamentBeans = new ArrayList<>();
        for (Map.Entry<Club, List<Tournament>> entry : notifications.entrySet()) {
            List<Tournament> tournaments = entry.getValue();
            for (Tournament tournament : tournaments) {
                tournamentBeans.add(TournamentMapper.toBean(tournament));
            }
        }
        return tournamentBeans;
    }

}
