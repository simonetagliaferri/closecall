package it.simonetagliaferri.model.domain;

import java.time.LocalDate;

public class TournamentRules {

    private String tournamentFormat;
    private String tournamentType;
    private String matchFormat;
    private String courtType;
    private int courtNumber;
    private double joinFee;
    private double courtPrice;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signupDeadline;

    public TournamentRules() {

    }

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

    public static LocalDate minimumStartDate() {
        return LocalDate.now().plusDays(2);
    }

    public static LocalDate minimumStartDate(LocalDate signupDeadline) {
        return signupDeadline.plusDays(1);
    }

    public static LocalDate minimumDeadline() {
        return LocalDate.now().plusDays(1);
    }

    public static LocalDate minimumDeadline(LocalDate startDate) {
        return startDate.minusDays(1);
    }

    public static LocalDate minimumEndDate(LocalDate startDate) {
        return startDate;
    }

}
