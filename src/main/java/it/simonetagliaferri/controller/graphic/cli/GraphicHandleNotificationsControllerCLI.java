package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HandleNotificationsLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.PlayerNotificationsView;

import java.util.List;

public class GraphicHandleNotificationsControllerCLI extends GraphicController {

    HandleNotificationsLogicController controller;
    PlayerNotificationsView view;

    public GraphicHandleNotificationsControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new HandleNotificationsLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO());
        this.view = new PlayerNotificationsView();
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
        view.listNotifications(tournaments);
    }

    public void startHost() {

    }
}
