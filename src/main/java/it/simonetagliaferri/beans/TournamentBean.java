package it.simonetagliaferri.beans;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class TournamentBean {
    private final static String DATE_FORMAT = "MM/dd/yyyy";

    private String name;
    private String tournamentType;
    private String tournamentFormat;
    private String matchFormat;
    private String courtType;
    private int teamsNumber;
    private ArrayList<Double> prizes;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signupDeadline;

    public TournamentBean() {

    }

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
    public void setTeamsNumber(int teamsNumber) {
        this.teamsNumber = teamsNumber;
    }
    public void setPrizes(ArrayList<Double> prizes) {
        this.prizes = prizes;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public void setSignupDeadline(LocalDate signupDeadline) {
        this.signupDeadline = signupDeadline;
    }


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
