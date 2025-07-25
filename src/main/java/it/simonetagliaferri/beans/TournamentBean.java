package it.simonetagliaferri.beans;


import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TournamentBean {
    private static final String DATE_FORMAT = "MM/dd/yyyy";

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
    private List<TeamBean> reservedTeams;
    private List<MatchBean> matches;
    private double joinFee;
    private double courtPrice;

    public TournamentBean() {
        confirmedTeams = new ArrayList<>();
    }


    public void setClub(ClubBean club) { this.club = club; }
    public void setTournamentName(String tournamentName) {
        this.name=tournamentName;
    }
    public void setTournamentType(String tournamentType) {
        this.tournamentType = tournamentType;
    }
    public void setTournamentFormat(String tournamentFormat) {
        this.tournamentFormat = tournamentFormat;
    }
    public void setMatchFormat(String matchFormat) {
        this.matchFormat = matchFormat;
    }
    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }
    public void setCourtNumber(int courtNumber) { this.courtNumber = courtNumber; }
    public void setTeamsNumber(int teamsNumber) {
        this.teamsNumber = teamsNumber;
    }
    public void setPrizes(List<Double> prizes) {
        this.prizes = prizes;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setSignupDeadline(LocalDate signupDeadline) {
        this.signupDeadline = signupDeadline;
    }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public void setConfirmedTeams(List<TeamBean> confirmedTeams) { this.confirmedTeams = confirmedTeams; }
    public void setReservedTeams(List<TeamBean> reservedTeams) { this.reservedTeams = reservedTeams; }
    public void setMatches(List<MatchBean> matches) { this.matches = matches; }
    public void setJoinFee(double joinFee) { this.joinFee = joinFee; }
    public void setCourtPrice(double courtPrice) { this.courtPrice = courtPrice; }
    public ClubBean getClub() { return club; }
    public String getTournamentName() { return name; }
    public String getTournamentType() { return tournamentType; }
    public String getTournamentFormat() { return tournamentFormat; }
    public String getMatchFormat() { return matchFormat; }
    public String getCourtType() { return courtType; }
    public int getCourtNumber() { return courtNumber; }
    public int getTeamsNumber() { return teamsNumber; }
    public List<Double> getPrizes() { return prizes; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getSignupDeadline() { return signupDeadline; }
    public double getJoinFee() { return joinFee; }
    public double getCourtPrice() { return courtPrice; }

    public LocalDate formatDate(String date) {
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate d = LocalDate.parse(date, df);
            return d;
        } catch (DateTimeException e) {
            return null;
        }
    }

    public String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }


    public LocalDate isDateValid(LocalDate date) {
        if (date.isBefore(LocalDate.now()) || date == null) {
            return null;
        }
        return date;
    }

    public boolean isStartDateValid(LocalDate startDate) {
        if (this.signupDeadline != null) {
            return startDate.isAfter(this.signupDeadline);
        }
        if (this.endDate != null) {
            return startDate.isBefore(this.endDate);
        }
        return true;
    }

    public boolean isDeadlineValid(LocalDate deadline) {
        if (this.startDate != null) {
            return deadline.isBefore(this.startDate);
        }
        return true;
    }

    public boolean isEndDateValid(LocalDate endDate) { return endDate.isAfter(this.startDate); }

    public boolean isSingles() {
        return this.tournamentType.equals("Men's singles") || this.tournamentType.equals("Women's singles");
    }

    public List<TeamBean> getConfirmedTeams() { return confirmedTeams; }
    public List<TeamBean> getReservedTeams() { return reservedTeams; }
    public List<MatchBean> getMatches() { return matches; }

    public void addTeam(TeamBean team) {
        confirmedTeams.add(team);
    }
}
