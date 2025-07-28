package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.invite.Invite;

import java.util.List;

public class JDBCInviteDAO implements InviteDAO {
    @Override
    public void save(Invite invite) {

    }

    @Override
    public List<Invite> getInvites(String playerUsername) {
        return null;
    }

    @Override
    public void delete(Invite invite) {

    }

    @Override
    public Invite getInvite(Player player, Tournament tournament) {
        return null;
    }
}
