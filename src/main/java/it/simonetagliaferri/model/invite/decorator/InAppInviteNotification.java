package it.simonetagliaferri.model.invite.decorator;

import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.invite.Invite;

public class InAppInviteNotification implements InviteNotification {

    private final InviteDAO inviteDAO;

    public InAppInviteNotification(InviteDAO inviteDAO) {
        this.inviteDAO = inviteDAO;
    }

    @Override
    public void send(Invite invite) {
        inviteDAO.save(invite);
    }
}
