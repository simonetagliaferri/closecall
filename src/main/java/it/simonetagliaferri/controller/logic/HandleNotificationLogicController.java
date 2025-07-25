package it.simonetagliaferri.controller.logic;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.infrastructure.SessionManager;
import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.utils.converters.InviteMapper;

import java.util.ArrayList;
import java.util.List;

public class HandleNotificationLogicController extends LogicController{

    InviteDAO inviteDAO;
    public HandleNotificationLogicController(SessionManager sessionManager, InviteDAO inviteDAO) {
        super(sessionManager);
        this.inviteDAO = inviteDAO;
    }

    public List<InviteBean> getInvites() {
        List<InviteBean> inviteBeanList = new ArrayList<>();
        List<Invite> invites = inviteDAO.getInvites(sessionManager.getCurrentUser().getUsername());
        if (invites != null && !invites.isEmpty()) {
            for (Invite invite : invites) {
                InviteBean inviteBean = InviteMapper.toBean(invite);
                inviteBeanList.add(inviteBean);
            }
        }
        return inviteBeanList;
    }
}
