package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.HostDAO;
import it.simonetagliaferri.model.dao.PlayerDAO;
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

public class HandleNotificationsApplicationController extends ApplicationController {

    private final PlayerDAO playerDAO;
    private final HostDAO hostDAO;

    public HandleNotificationsApplicationController(SessionManager sessionManager, PlayerDAO playerDAO, HostDAO hostDAO) {
        super(sessionManager);
        this.playerDAO = playerDAO;
        this.hostDAO = hostDAO;
    }

    private Player loadPlayer() {
        String username = getCurrentUserUsername();
        return playerDAO.findByUsername(username);
    }

    private Host loadHost() {
        String username = getCurrentUserUsername();
        return hostDAO.getHostByUsername(username);
    }

    public List<TournamentBean> getPlayerNotifications() {
        Player player = loadPlayer();
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
        Player player = loadPlayer();
        player.clearNotifications();
        playerDAO.savePlayer(player);
    }

    public Map<TournamentBean, List<PlayerBean>> getHostNotifications() {
        Host host = loadHost();
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
        Host host = loadHost();
        host.clearNotifications();
        hostDAO.saveHost(host);
    }

}
