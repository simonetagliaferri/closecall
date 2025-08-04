package it.simonetagliaferri.model.domain;

public class TournamentRules {

    private final String tournamentFormat;
    private final String tournamentType;
    private String matchFormat;
    private String courtType;
    private int courtNumber;
    private int teamsNumber;
    private double joinFee;
    private double courtPrice;

    public TournamentRules(String tournamentFormat, String tournamentType, String matchFormat, String courtType, int courtNumber, int teamsNumber) {
        this.tournamentFormat = tournamentFormat;
        this.tournamentType = tournamentType;
        this.matchFormat = matchFormat;
        this.courtType = courtType;
        this.courtNumber = courtNumber;
        this.teamsNumber = teamsNumber;
    }

    public TournamentRules(String tournamentFormat, String tournamentType) {
        this.tournamentFormat = tournamentFormat;
        this.tournamentType = tournamentType;
    }

    public void setTournamentCosts(double joinFee, double courtPrice) {
        this.joinFee = joinFee;
        this.courtPrice = courtPrice;
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
    public int getTeamsNumber() {
        return teamsNumber;
    }
    public double getJoinFee() {
        return joinFee;
    }
    public double getCourtPrice() {
        return courtPrice;
    }
}
