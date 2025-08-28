package it.simonetagliaferri.model.invite;

import it.simonetagliaferri.model.domain.Player;
import it.simonetagliaferri.model.domain.Tournament;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Invite implements Serializable {
    private Tournament tournament;
    private final Player player;
    private LocalDate sendDate;
    private LocalDate expiryDate;
    private InviteStatus status;
    private String message;

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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Invite)) return false;
        Invite invite = (Invite) o;
        if (invite == this) return true;
        return Objects.equals(getTournament(), invite.getTournament()) && Objects.equals(getPlayer(), invite.getPlayer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTournament(), getPlayer());
    }
}
