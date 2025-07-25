package it.simonetagliaferri.service;

import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.decorator.EmailDecorator;
import it.simonetagliaferri.model.invite.decorator.InAppInviteNotification;
import it.simonetagliaferri.model.invite.decorator.InviteNotification;

public class InviteService {
    private final InviteDAO inviteDAO;

    public InviteService(InviteDAO inviteDAO) {
        this.inviteDAO = inviteDAO;
    }

    public void sendInvite(Invite invite, boolean email) {
        InviteNotification notifier = buildNotifier(email);
        notifier.send(invite);
    }

    private InviteNotification buildNotifier(boolean email) {
        InviteNotification base = new InAppInviteNotification(inviteDAO);
        if (email) {
            base = new EmailDecorator(base);
        }
        return base;
    }

}
