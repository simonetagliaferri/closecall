package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.utils.converters.PlayerMapper;
import it.simonetagliaferri.utils.converters.TournamentMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HandleNotificationsLogicController extends LogicController {

    PlayerDAO playerDAO;
    TournamentDAO tournamentDAO;
    HostDAO hostDAO;

    public HandleNotificationsLogicController(SessionManager sessionManager, PlayerDAO playerDAO, HostDAO hostDAO, TournamentDAO tournamentDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.hostDAO = hostDAO;
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

    public void clearPlayerNotifications() {
        Player player = playerDAO.findByUsername(getCurrentUser().getUsername());
        player.clearNotifications();
    }

    public Map<TournamentBean, List<PlayerBean>> getHostNotifications() {
        Host host = hostDAO.getHostByUsername(getCurrentUser().getUsername());
        Map<Tournament, List<Player>> notifications = host.getNewPlayers();
        Map<TournamentBean, List<PlayerBean>> hostNotifications = new HashMap<>();
        List<PlayerBean> playerBeans = new ArrayList<>();
        TournamentBean tournamentBean;
        for (Map.Entry<Tournament, List<Player>> entry : notifications.entrySet()) {
            List<Player> players = entry.getValue();
            for (Player player : players) {
                playerBeans.add(PlayerMapper.toBean(player));
            }
            tournamentBean = TournamentMapper.toBean(entry.getKey());
            hostNotifications.put(tournamentBean, playerBeans);
        }
        return hostNotifications;
    }

    public void clearHostNotifications() {
        Host host = hostDAO.getHostByUsername(getCurrentUser().getUsername());
        host.clearNotifications();
    }

}
