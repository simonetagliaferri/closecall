package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.controller.logic.HandleHostNotificationLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.HandleHostNotificationView;

public class GraphicHandleHostNotificationControllerCLI extends GraphicHandleNotificationControllerCLI{

    HandleHostNotificationLogicController controller;
    HandleHostNotificationView view;

    public GraphicHandleHostNotificationControllerCLI(AppContext appContext) {
        super(appContext);
        this.controller = new HandleHostNotificationLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getInviteDAO());
        this.view = new HandleHostNotificationView();
    }

    public void start() {

    }
}
