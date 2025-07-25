package it.simonetagliaferri.utils.converters;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;

public class InviteMapper {

    public static Invite fromBean(InviteBean inviteBean) {
        Tournament tournament = TournamentMapper.fromBean(inviteBean.getTournament());
        Player player = PlayerMapper.fromBean(inviteBean.getPlayer());
        return new Invite(tournament, player, inviteBean.getSendDate(), inviteBean.getExpiryDate(), inviteBean.getStatus(), inviteBean.getMessage());
    }

    public static InviteBean toBean(Invite invite) {
        TournamentBean tournamentBean = TournamentMapper.toBean(invite.getTournament());
        PlayerBean playerBean = PlayerMapper.toBean(invite.getPlayer());
        return new InviteBean(tournamentBean, playerBean, invite.getSendDate(), invite.getExpiryDate(), invite.getStatus(), invite.getMessage());
    }
}
