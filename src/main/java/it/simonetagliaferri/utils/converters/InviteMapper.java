package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.invite.Invite;

public class InviteMapper {

    private InviteMapper() {
    }

    public static InviteBean toBean(Invite invite) {
        TournamentBean tournamentBean = TournamentMapper.toBean(invite.getTournament());
        PlayerBean playerBean = PlayerMapper.toBean(invite.getPlayer());
        return new InviteBean(tournamentBean, playerBean, invite.getSendDate(), invite.getExpiryDate(), invite.getStatus(), invite.getMessage());
    }
}
