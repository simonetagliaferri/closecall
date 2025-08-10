package it.simonetagliaferri.model.invite.decorator;

import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.invite.Invite;

public class InAppInviteNotification implements InviteNotification {

    @Override
    public void send(Invite invite) {
        Player player = invite.getPlayer();
        player.addInvite(invite);
    }
}
