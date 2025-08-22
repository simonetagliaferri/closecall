package it.simonetagliaferri.model.domain;

import java.io.Serializable;
import java.time.LocalDate;

public class TournamentRules implements Serializable {

    private final String tournamentFormat;
    private final String tournamentType;
    private String matchFormat;
    private String courtType;
    private int courtNumber;
    private double joinFee;
    private double courtPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signupDeadline;

    public TournamentRules(String tournamentFormat, String tournamentType, String matchFormat, String courtType, int courtNumber) {
        this.tournamentFormat = tournamentFormat;
        this.tournamentType = tournamentType;
        this.matchFormat = matchFormat;
        this.courtType = courtType;
        this.courtNumber = courtNumber;
    }

    public TournamentRules(String tournamentFormat, String tournamentType, LocalDate startDate) {
        this.tournamentFormat = tournamentFormat;
        this.tournamentType = tournamentType;
        this.startDate = startDate;
    }

    public void setTournamentCosts(double joinFee, double courtPrice) {
        this.joinFee = joinFee;
        this.courtPrice = courtPrice;
    }

    public void setTournamentDates(LocalDate startDate, LocalDate signupDeadline, LocalDate endDate) {
        this.startDate = startDate;
        this.signupDeadline = signupDeadline;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getSignupDeadline() {
        return signupDeadline;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getTournamentFormat() {
        return tournamentFormat;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public String getMatchFormat() {
        return matchFormat;
    }

    public String getCourtType() {
        return courtType;
    }

    public int getCourtNumber() {
        return courtNumber;
    }

    public double getJoinFee() {
        return joinFee;
    }

    public double getCourtPrice() {
        return courtPrice;
    }

}
