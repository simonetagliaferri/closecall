package it.simonetagliaferri.beans;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TournamentBean {

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
    private ClubBean club;
    private List<TeamBean> confirmedTeams;
    private List<TeamBean> pendingTeams;
    private List<TeamBean> partialTeams;
    private double joinFee;
    private double courtPrice;

    public TournamentBean() {
        confirmedTeams = new ArrayList<>();
        pendingTeams = new ArrayList<>();
        partialTeams = new ArrayList<>();
    }

    public ClubBean getClub() {
        return club;
    }

    public void setClub(ClubBean club) {
        this.club = club;
    }

    public String getTournamentName() {
        return name;
    }

    public void setTournamentName(String tournamentName) {
        this.name = tournamentName;
    }

    public String getTournamentType() {
        return tournamentType;
    }

    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }

    public String getTournamentFormat() {
        return tournamentFormat;
    }

    public void setTournamentFormat(String tournamentFormat) {
        this.tournamentFormat = tournamentFormat;
    }

    public String getMatchFormat() {
        return matchFormat;
    }

    public void setMatchFormat(String matchFormat) {
        this.matchFormat = matchFormat;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    public int getCourtNumber() {
        return courtNumber;
    }

    public void setCourtNumber(int courtNumber) {
        this.courtNumber = courtNumber;
    }

    public int getTeamsNumber() {
        return teamsNumber;
    }

    public void setTeamsNumber(int teamsNumber) {
        this.teamsNumber = teamsNumber;
    }

    public List<Double> getPrizes() {
        return prizes;
    }

    public void setPrizes(List<Double> prizes) {
        this.prizes = prizes;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getSignupDeadline() {
        return signupDeadline;
    }

    public void setSignupDeadline(LocalDate signupDeadline) {
        this.signupDeadline = signupDeadline;
    }

    public double getJoinFee() {
        return joinFee;
    }

    public void setJoinFee(double joinFee) {
        this.joinFee = joinFee;
    }

    public double getCourtPrice() {
        return courtPrice;
    }

    public void setCourtPrice(double courtPrice) {
        this.courtPrice = courtPrice;
    }

    public String getClubName() {
        return club.getName();
    }

    public int getAvailableSpots() {
        return teamsNumber - confirmedTeams.size() - partialTeams.size() / 2 - pendingTeams.size();
    }

    public boolean isSingles() {
        return this.tournamentType.equals("Men's singles") || this.tournamentType.equals("Women's singles");
    }

    public List<TeamBean> getConfirmedTeams() {
        return confirmedTeams;
    }

    public void setConfirmedTeams(List<TeamBean> confirmedTeams) {
        this.confirmedTeams = confirmedTeams;
    }

    public List<TeamBean> getPendingTeams() {
        return pendingTeams;
    }

    public void setPendingTeams(List<TeamBean> pendingTeams) {
        this.pendingTeams = pendingTeams;
    }

    public List<TeamBean> getPartialTeams() {
        return partialTeams;
    }

    public void setPartialTeams(List<TeamBean> partialTeams) {
        this.partialTeams = partialTeams;
    }
}
