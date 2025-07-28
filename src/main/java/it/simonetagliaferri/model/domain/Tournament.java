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
    private final List<Team> pendingTeams;
    private final List<Team> partialTeams;
    private final List<Match> matches;
    private double joinFee;
    private double courtPrice;

    private String id = "";

    public Tournament() {
        this.matches = new ArrayList<>();
        this.confirmedTeams = new ArrayList<>();
        this.pendingTeams = new ArrayList<>();
        this.partialTeams = new ArrayList<>();
    }

    public Tournament(String tournamentName, String tournamentType, String tournamentFormat, String matchFormat,
                      String courtType, int courtNumber, int teamsNumber, List<Double> prizes, LocalDate startDate,
                      LocalDate endDate, LocalDate signupDeadline, Club club, List<Team> confirmedTeams, List<Team> pendingTeams, List<Team> partialTeams, List<Match> matches, double joinFee, double courtPrice) {
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
        this.pendingTeams = pendingTeams;
        this.partialTeams = partialTeams;
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
        return this.teamsNumber - (this.confirmedTeams.size()+this.pendingTeams.size()+this.partialTeams.size()/2);
    }

    public void reserveSpot(Player... players) {
        Team team;
        if (players.length == 1) {
            team = new Team(players[0], getTeamType());
            this.pendingTeams.add(team);
            if (!isSingles())
                this.partialTeams.add(team);
        }
        else if (players.length == 2) {
            team = new Team(players[0], players[1]);
            this.pendingTeams.add(team);
        }
        else {
            throw new IllegalArgumentException("A team must have either 1 or 2 players.");
        }
    }

    public List<Team> getConfirmedTeams() {
        return this.confirmedTeams;
    }
    public List<Team> getPendingTeams() { return this.pendingTeams; }

    public List<Team> getPartialTeams() { return this.partialTeams; }

    public boolean isSingles() {
        return this.tournamentType.equals("Men's singles") || this.tournamentType.equals("Women's singles");
    }

    public Team getReservedTeam(Player player) {
        for (Team team : this.pendingTeams) {
            for (Player p : team.getPlayers()) {
                if (p.getUsername().equals(player.getUsername())) {
                    return team;
                }
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

    public void processInviteForSingles(Invite invite, Team team) {
        if (!pendingTeams.contains(team)) return;
        switch (invite.getStatus()) {
            case ACCEPTED:
                pendingTeams.remove(team);
                confirmedTeams.add(team);
                break;
            case DECLINED:
            case REVOKED:
            case EXPIRED:
                pendingTeams.remove(team);
                break;
        }
    }

    public void processInviteForDoubles(Invite invite, Team team, Invite teammateInvite) {
        if (!pendingTeams.contains(team)) return;
        InviteStatus s1 = invite.getStatus();
        InviteStatus s2 = teammateInvite != null ? teammateInvite.getStatus() : null;
        if (s2 == null && s1 == InviteStatus.ACCEPTED) {
            pendingTeams.remove(team);
            if (team.isFull()) {
                confirmedTeams.add(team);
            }
            else {
                partialTeams.add(team);
            }
        }
        else if (s2 == null && (s1 == InviteStatus.DECLINED || s1 == InviteStatus.REVOKED || s1 == InviteStatus.EXPIRED)) {
            pendingTeams.remove(team);
            if (team.isFull()) {
                team.removePlayer(invite.getPlayer());
                partialTeams.add(team);
            }
        }
        else if (s1 == InviteStatus.DECLINED || s1 == InviteStatus.REVOKED || s1 == InviteStatus.EXPIRED ||
                s2 == InviteStatus.DECLINED || s2 == InviteStatus.REVOKED || s2 == InviteStatus.EXPIRED) {
            pendingTeams.remove(team);
        }
        else if (s1 == InviteStatus.ACCEPTED && s2 == InviteStatus.ACCEPTED) {
            pendingTeams.remove(team);
            confirmedTeams.add(team);
        }
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
