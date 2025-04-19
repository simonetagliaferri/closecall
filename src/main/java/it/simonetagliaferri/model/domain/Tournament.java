package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.beans.TournamentBean;

import java.time.LocalDate;
import java.util.List;

public class Tournament {
    private final static int MATCHES_A_DAY_PER_COURT=3;
    private String name;
    private String tournamentType;
    private String tournamentFormat;
    private String matchFormat;
    private String courtType;
    private int courtNumber;
    private int teamsNumber;
    private List<Double> prizes;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signupDeadline;
    private String hostUsername;

    public Tournament(TournamentBean tournament) {
        this.name=tournament.getTournamentName();
        this.tournamentType = tournament.getTournamentType();
        this.tournamentFormat = tournament.getTournamentFormat();
        this.matchFormat = tournament.getMatchFormat();
        this.courtType = tournament.getCourtType();
        this.courtNumber = tournament.getCourtNumber();
        this.teamsNumber = tournament.getTeamsNumber();
        this.prizes = tournament.getPrizes();
        this.startDate = tournament.getStartDate();
        this.signupDeadline = tournament.getSignupDeadline();
        this.hostUsername = tournament.getHostUsername();
    }

    public String getHostUsername() { return hostUsername; }
    public String getTournamentName() { return name; }

    // It needs to be reviewed.
    public LocalDate estimateEndDate() {
        int matchNum=0;
        int matchesADay;
        int matchNumNoFinals;
        int days;
        int rounds;
        matchesADay = this.courtNumber * MATCHES_A_DAY_PER_COURT;
        if (tournamentFormat=="RoundRobin") {
            matchNum = this.teamsNumber*(this.teamsNumber-1)/2;
            if (matchesADay<=this.teamsNumber) { // Every team plays a match a day.
                days=matchNum/matchesADay;
            }
            else {
                days=matchNum/this.teamsNumber;
            }
        }
        else {
            rounds = log2(this.teamsNumber);
            System.out.println(rounds);
            if (tournamentFormat=="Single-elimination") {
                matchNum = this.teamsNumber-1;
            }
            else if (tournamentFormat=="Double-elimination") {
                matchNum=(this.teamsNumber*2)-1;
            }
            matchNumNoFinals=matchNum-2;
            if (matchesADay<=this.teamsNumber) {
                days=(matchNumNoFinals/(matchesADay/rounds))+2;
            }
            else {
                days=(matchNumNoFinals/(this.teamsNumber/rounds))+2;
            }
        }
        return this.startDate.plusDays(days);
    }


    private int log2(int n) {
        return ((Double)(Math.ceil(Math.log(n)/Math.log(2)))).intValue();
    }
}
