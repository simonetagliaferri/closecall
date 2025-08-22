package it.simonetagliaferri.model.invite;

import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.io.Serializable;
import java.time.LocalDate;

public class Invite implements Serializable {
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

    public Invite(Tournament tournament, Player player) {
        this.tournament = tournament;
        this.player = player;
    }

    public void updateTournament(Tournament tournament) {
        this.tournament = tournament;
    }


    public void updateStatus(InviteStatus inviteStatus) {
        this.status = inviteStatus;
    }

    public Tournament getTournament() {
        return tournament;
    }

    public Player getPlayer() {
        return this.player;
    }

    public LocalDate getSendDate() {
        return sendDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean hasExpired() {
        if (expiryDate.isBefore(LocalDate.now())) {
            if (status != InviteStatus.EXPIRED)
                updateStatus(InviteStatus.EXPIRED);
            return true;
        }
        return false;
    }

    public InviteStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSameAs(Invite invite) {
        return this.tournament.isSameAs(invite.getTournament()) &&
                this.player.isSameAs(invite.getPlayer());
    }

}
