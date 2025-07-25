package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.invite.Invite;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InMemoryInviteDAO implements InviteDAO {

    private final Map<String, List<Invite>> invites;

    public InMemoryInviteDAO(Map<String, List<Invite>> invites) {
        this.invites = invites;
    }

    @Override
    public void save(Invite invite) {
        List<Invite> invites = this.invites.get(invite.getPlayer().getUsername());
        if (invites == null) {
            invites = new ArrayList<>();
        }
        invites.add(invite);
        this.invites.put(invite.getPlayer().getUsername(), invites);
    }

    @Override
    public List<Invite> getInvites(String playerUsername) {
        return this.invites.get(playerUsername);
    }
}
