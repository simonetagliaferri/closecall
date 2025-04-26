package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tournament {
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
    private TournamentFormatStrategy tournamentFormatStrategy;
    private List<Team> teams;

    public Tournament() {
        this.teams = new ArrayList<>();
    }

    public Tournament(String tournamentName, String tournamentType, String tournamentFormat, String matchFormat,
                      String courtType, int courtNumber, int teamsNumber, List<Double> prizes, LocalDate startDate,
                      LocalDate endDate, LocalDate signupDeadline, String hostUsername, List<Team> teams) {
        this.name = tournamentName;
        this.tournamentType = tournamentType;
        this.tournamentFormat = tournamentFormat;
        this.matchFormat = matchFormat;
        this.courtType = courtType;
        this.courtNumber = courtNumber;
        this.teamsNumber = teamsNumber;
        this.prizes = prizes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signupDeadline = signupDeadline;
        this.hostUsername = hostUsername;
        this.teams = teams;
    }

    public String getHostUsername() { return hostUsername; }
    public String getTournamentName() { return name; }

    public void setTournamentFormatStrategy(TournamentFormatStrategy strategy) {
        this.tournamentFormatStrategy = strategy;
    }
    public LocalDate estimateEndDate() {
        int days = this.tournamentFormatStrategy.estimateNeededDays(this.teamsNumber, this.courtNumber);
        return this.startDate.plusDays(days);
    }

    public int availableSpots() {
        return this.teamsNumber - this.teams.size();
    }

    public Team addTeam(Player... players) {
        Team team;
        if (players.length == 1 && isSingles()) {
            team = new Team(players[0]);
            this.teams.add(team);
        }
        else if (players.length == 2 && !isSingles()) {
            team = new Team(players[0], players[1]);
            this.teams.add(team);
        }
        else {
            throw new IllegalArgumentException("A team must have either 1 or 2 players.");
        }
        return team;
    }

    public List<Team> getTeams() {
        return this.teams;
    }

    public boolean isSingles() {
        return this.tournamentType.equals("Men's singles") || this.tournamentType.equals("Women's singles");
    }

    public String getTournamentType() { return tournamentType; }
    public String getMatchFormat() { return matchFormat; }
    public String getCourtType() { return courtType; }
    public int getCourtNumber() { return courtNumber; }
    public int getTeamsNumber() { return teamsNumber; }
    public String getTournamentFormat() { return tournamentFormat; }
    public List<Double> getPrizes() { return prizes; }
    public LocalDate getStartDate() { return startDate; }
    public LocalDate getEndDate() { return endDate; }
    public LocalDate getSignupDeadline() { return signupDeadline; }

}
