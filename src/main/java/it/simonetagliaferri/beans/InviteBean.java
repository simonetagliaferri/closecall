package it.simonetagliaferri.beans;
import it.simonetagliaferri.model.invite.InviteStatus;

import java.time.LocalDate;

public class InviteBean {
    TournamentBean tournament;
    PlayerBean player;
    LocalDate sendDate;
    LocalDate expiryDate;
    InviteStatus status;
    String message;
    boolean sendEmail;

    public InviteBean(TournamentBean tournament, PlayerBean player, LocalDate sendDate, LocalDate expiryDate, InviteStatus status, String message) {
        this.tournament = tournament;
        this.player = player;
        this.sendDate = sendDate;
        this.expiryDate = expiryDate;
        this.status = status;
        this.message = message;
    }

    public InviteBean(PlayerBean player, LocalDate expiryDate, String message, boolean sendEmail) {
        this.player = player;
        this.expiryDate = expiryDate;
        this.message = message;
        this.sendEmail = sendEmail;
    }


    public void setTournament(TournamentBean tournament) {
        this.tournament = tournament;
    }
    public void setPlayer(PlayerBean player) {
        this.player = player;
    }
    public void setSendDate(LocalDate sendDate) {
        this.sendDate = sendDate;
    }
    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
    public void setStatus(InviteStatus status) {
        this.status = status;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public TournamentBean getTournament() {
        return tournament;
    }
    public PlayerBean getPlayer() {
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
    public boolean getSendEmail() {
        return sendEmail;
    }
    public void setSendEmail(boolean sendEmail) {
        this.sendEmail = sendEmail;
    }

}
