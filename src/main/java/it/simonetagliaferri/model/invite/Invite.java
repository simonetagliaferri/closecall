package it.simonetagliaferri.model.invite;

import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.time.LocalDate;

public class Invite {
    Tournament tournament;
    Player player;
    LocalDate sendDate;
    LocalDate expiryDate;
    InviteStatus status;
    String message;

    public Invite(Tournament tournament, Player player, LocalDate sendDate, LocalDate expiryDate, InviteStatus status, String message) {
        this.tournament = tournament;
        this.player = player;
        this.sendDate = sendDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.message = message;
    }

    public void updateStatus(InviteStatus inviteStatus){
        this.status = inviteStatus;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Player getPlayer() {
        return player;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }
    public LocalDate getExpiryDate() {
        return expiryDate;
    }
    public InviteStatus getStatus() {
        return status;
    }
    public String getMessage() {
        return message;
    }
}
