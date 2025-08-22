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

    public TournamentBean getTournament() {
        return tournament;
    }

    public void setTournament(TournamentBean tournament) {
        this.tournament = tournament;
    }

    public PlayerBean getPlayer() {
        return player;
    }

    public void setPlayer(PlayerBean player) {
        this.player = player;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getSendEmail() {
        return sendEmail;
    }

}
