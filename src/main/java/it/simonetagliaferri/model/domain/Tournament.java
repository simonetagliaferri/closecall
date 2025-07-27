package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
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
    private Club club;
    private TournamentFormatStrategy tournamentFormatStrategy;
    private final List<Team> confirmedTeams;
    private final List<Team> reservedTeams;
    private final List<Match> matches;
    private double joinFee;
    private double courtPrice;

    private String id = "";

    public Tournament() {
        this.matches = new ArrayList<>();
        this.confirmedTeams = new ArrayList<>();
        this.reservedTeams = new ArrayList<>();
    }

    public Tournament(String tournamentName, String tournamentType, String tournamentFormat, String matchFormat,
                      String courtType, int courtNumber, int teamsNumber, List<Double> prizes, LocalDate startDate,
                      LocalDate endDate, LocalDate signupDeadline, Club club, List<Team> confirmedTeams, List<Team> reservedTeams, List<Match> matches, double joinFee, double courtPrice) {
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
        this.club = club;
        this.confirmedTeams = confirmedTeams;
        this.reservedTeams = reservedTeams;
        this.matches = matches;
        this.joinFee = joinFee;
        this.courtPrice = courtPrice;
    }

    public Club getClub() { return club; }
    public String getTournamentName() { return name; }

    public void setTournamentFormatStrategy(TournamentFormatStrategy strategy) {
        this.tournamentFormatStrategy = strategy;
    }
    public LocalDate estimateEndDate() {
        int days = this.tournamentFormatStrategy.estimateNeededDays(this.teamsNumber, this.courtNumber);
        return this.startDate.plusDays(days);
    }

    public int availableSpots() {
        return this.teamsNumber - (this.confirmedTeams.size()+this.reservedTeams.size());
    }

    public Team addTeam(Player... players) {
        Team team;
        if (players.length == 1) {
            team = new Team(players[0], getTeamType());
            this.confirmedTeams.add(team);
        }
        else if (players.length == 2) {
            team = new Team(players[0], players[1]);
            this.confirmedTeams.add(team);
        }
        else {
            throw new IllegalArgumentException("A team must have either 1 or 2 players.");
        }
        return team;
    }

    public Team reserveSpot(Player... players) {
        Team team;
        if (players.length == 1) {
            team = new Team(players[0], getTeamType());
            this.reservedTeams.add(team);
        }
        else if (players.length == 2) {
            team = new Team(players[0], players[1]);
            this.reservedTeams.add(team);
        }
        else {
            throw new IllegalArgumentException("A team must have either 1 or 2 players.");
        }
        return team;
    }

    public List<Team> getConfirmedTeams() {
        return this.confirmedTeams;
    }
    public List<Team> getReservedTeams() { return this.reservedTeams; }

    public boolean isSingles() {
        return this.tournamentType.equals("Men's singles") || this.tournamentType.equals("Women's singles");
    }

    public Team getReservedTeam(String player) {
        for (Team team : this.reservedTeams) {
            if (team.getPlayers().contains(player)) {
                return team;
            }
        }
        return null;
    }

    public TeamType getTeamType() {
        if (isSingles()) {
            return TeamType.SINGLE;
        }
        else return TeamType.DOUBLE;
    }

    public void processInvite(Invite invite) {
        for (Team team : this.reservedTeams) {
            if (team.getPlayers().contains(invite.getPlayer())) {
                if (isSingles()) processSingleInvite(invite, team);
                else processDoubleInvite(invite, team);
            }
        }
    }
    private void processSingleInvite(Invite invite, Team team) {
        if (invite.getStatus().equals(InviteStatus.ACCEPTED)) {
            this.reservedTeams.remove(team);
            this.confirmedTeams.add(team);
        }
        else if (invite.getStatus().equals(InviteStatus.DECLINED) || invite.getStatus().equals(InviteStatus.REVOKED)) {
            this.reservedTeams.remove(team);
        }
    }
    private void processDoubleInvite(Invite invite, Team team) {

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
    public double getJoinFee() { return joinFee; }
    public double getCourtPrice() { return courtPrice; }
    public void setId(String id) { this.id = id; }
    public String getId() { return id; }
    public List<Match> getMatches() { return matches; }
}
