package it.simonetagliaferri.model.invite.decorator;

import it.simonetagliaferri.model.invite.Invite;

abstract public class InviteNotificationDecorator implements InviteNotification {

    protected InviteNotification wrappee;

    public InviteNotificationDecorator(InviteNotification notification) {
        wrappee = notification;
    }

    @Override
    public void send(Invite invite) {
        wrappee.send(invite);
    }
}
