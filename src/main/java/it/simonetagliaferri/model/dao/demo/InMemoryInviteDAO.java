package it.simonetagliaferri.model.dao.demo;

import it.simonetagliaferri.model.dao.InviteDAO;
import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;
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

    @Override
    public void delete(Invite invite) {
        List<Invite> invites = this.invites.get(invite.getPlayer().getUsername());
        if (invites != null) {
            invites.remove(invite);
        }
    }

    @Override
    public Invite getInvite(Player player, Tournament tournament) {
        List<Invite> invites = this.invites.get(player.getUsername());
        if (invites != null) {
            for (Invite invite : invites) {
                if (invite.getTournament() == tournament) {
                    return invite;
                }
            }
        }
        return null;
    }
}
