package it.simonetagliaferri.model.invite.decorator;

import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.invite.Invite;

public class InAppInviteNotification implements InviteNotification {

    private final PlayerDAO playerDAO;

    public InAppInviteNotification(PlayerDAO playerDAO) {
        this.playerDAO = playerDAO;
    }

    @Override
    public void send(Invite invite) {
        Player player = invite.getPlayer();
        player.addInvite(invite);
        playerDAO.savePlayer(player);
    }
}
