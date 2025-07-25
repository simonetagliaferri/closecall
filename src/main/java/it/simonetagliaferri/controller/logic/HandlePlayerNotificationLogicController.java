package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.utils.converters.InviteMapper;

public class HandlePlayerNotificationLogicController extends HandleNotificationLogicController{

    public HandlePlayerNotificationLogicController(SessionManager sessionManager, InviteDAO inviteDAO) {
        super(sessionManager, inviteDAO);
    }

    public void updateInvite(InviteBean inviteBean, InviteStatus status){
        Invite invite = InviteMapper.fromBean(inviteBean);
        invite.updateStatus(status);
        inviteDAO.save(invite);
        Tournament tournament = invite.getTournament();

    }

}
