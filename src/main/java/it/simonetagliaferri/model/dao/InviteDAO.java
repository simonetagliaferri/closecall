package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.invite.Invite;

import java.util.List;

public interface InviteDAO {

    void save(Invite invite);

    List<Invite> getInvites(String playerUsername);
}
