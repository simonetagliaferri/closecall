package it.simonetagliaferri.beans;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class TournamentBean {
    private final static String DATE_FORMAT = "MM/dd/yyyy";

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

    public TournamentBean() {

    }
    public void setHostUsername(String hostUsername) { this.hostUsername = hostUsername; }
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
    public String getHostUsername() { return hostUsername; }
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


    public static LocalDate isDateValid(String date) {
        try {
            DateTimeFormatter df = DateTimeFormatter.ofPattern(DATE_FORMAT);
            LocalDate d = LocalDate.parse(date, df);
            if (d.isBefore(LocalDate.now())) {
                return null;
            }
            return d;
        } catch (DateTimeException e) {
            return null;
        }
    }

    public boolean isDeadlineValid(LocalDate deadline) {
        return !deadline.isAfter(this.startDate);
    }
}
