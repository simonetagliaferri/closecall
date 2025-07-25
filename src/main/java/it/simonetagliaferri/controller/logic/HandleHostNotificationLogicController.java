package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.InviteDAO;

public class HandleHostNotificationLogicController extends HandleNotificationLogicController{

    public HandleHostNotificationLogicController(SessionManager sessionManager, InviteDAO inviteDAO) {
        super(sessionManager, inviteDAO);
    }
}
