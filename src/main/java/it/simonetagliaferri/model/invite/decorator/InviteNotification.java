package it.simonetagliaferri.model.invite.decorator;

import it.simonetagliaferri.model.invite.Invite;

public interface InviteNotification {

    void send(Invite invite);
}
