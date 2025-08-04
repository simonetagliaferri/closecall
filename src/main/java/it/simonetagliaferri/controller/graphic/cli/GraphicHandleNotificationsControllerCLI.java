package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HandleNotificationsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.HostNotificationsView;
import it.simonetagliaferri.view.cli.PlayerNotificationsView;

import java.util.List;
import java.util.Map;

public class GraphicHandleNotificationsControllerCLI extends GraphicController {

    HandleNotificationsLogicController controller;
    PlayerNotificationsView playerView;
    HostNotificationsView hostView;

    public GraphicHandleNotificationsControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new HandleNotificationsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getHostDAO());
        this.playerView = new PlayerNotificationsView();
        this.hostView = new HostNotificationsView();
    }

    public void start(Role role) {
        if (role == Role.PLAYER) {
            startPlayer();
        }
        else {
            startHost();
        }
    }

    public void startPlayer() {
        List<TournamentBean> tournaments = this.controller.getPlayerNotifications();
        playerView.listNotifications(tournaments);
        this.controller.clearPlayerNotifications();
    }

    public void startHost() {
        Map<TournamentBean, List<PlayerBean>> notifications = this.controller.getHostNotifications();
        for (TournamentBean tournamentBean : notifications.keySet()) {
            hostView.tournament(tournamentBean);
            for (PlayerBean playerBean : notifications.get(tournamentBean)) {
                hostView.newPlayer(playerBean);
            }
        }
        this.controller.clearHostNotifications();
    }
}
