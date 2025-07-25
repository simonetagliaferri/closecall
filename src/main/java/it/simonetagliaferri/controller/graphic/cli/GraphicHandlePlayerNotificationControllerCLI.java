package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.controller.logic.HandlePlayerNotificationLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.HandlePlayerNotificationView;

import java.util.List;

public class GraphicHandlePlayerNotificationControllerCLI extends GraphicHandleNotificationControllerCLI {

    HandlePlayerNotificationLogicController controller;
    HandlePlayerNotificationView view;

    public GraphicHandlePlayerNotificationControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new HandlePlayerNotificationLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getInviteDAO());
        this.view = new HandlePlayerNotificationView();
    }

    public void start() {
        List<InviteBean> invites = this.controller.getInvites();
        int choice = view.listNotifications(invites);
        if (choice == 0 ) return;
        InviteBean invite = invites.get(choice-1);
        view.expandedInvite(invite);
        this.controller.updateInvite(invite, view.handleInvite());
    }
}
