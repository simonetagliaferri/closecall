package it.simonetagliaferri.model.invite.decorator;

import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.service.EmailNotifier;

public class EmailDecorator extends InviteNotificationDecorator {

    private final EmailNotifier notifier;

    public EmailDecorator(InviteNotification notification) {
        super(notification);
        notifier = new EmailNotifier();
    }

    @Override
    public void send(Invite invite) {
        wrappee.send(invite);
        notifier.sendEmail(invite);
    }
}
