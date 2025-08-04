package it.simonetagliaferri.model.domain;

import com.fasterxml.jackson.annotation.*;
import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.model.observer.Publisher;
import it.simonetagliaferri.model.observer.Subscriber;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategyFactory;
import it.simonetagliaferri.view.cli.JoinTournamentView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Tournament implements Publisher {
    private String name;
    private TournamentRules tournamentRules;
    private List<Double> prizes;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signupDeadline;
    private Club club;
    private TournamentFormatStrategy tournamentFormatStrategy;
    private TeamRegistry teamRegistry;
    private List<Player> participants;
    private Subscriber host;

    public Tournament() {}

    public Tournament(String name, String tournamentFormat, String tournamentType, LocalDate startDate, Club club) {
        this.name = name;
        this.tournamentRules = new TournamentRules(tournamentFormat, tournamentType);
        this.startDate = startDate;
        this.club = club;
    }

    public Tournament(String name, String tournamentType, String tournamentFormat, String matchFormat,
                      String courtType, int courtNumber, int teamsNumber, List<Double> prizes, LocalDate startDate,
                      LocalDate endDate, LocalDate signupDeadline, Club club, double joinFee, double courtPrice) {
        this.name = name;
        this.tournamentRules = new TournamentRules(tournamentFormat, tournamentType, matchFormat, courtType, courtNumber, teamsNumber);
        this.tournamentRules.setTournamentCosts(joinFee, courtPrice);
        this.prizes = prizes;
        this.startDate = startDate;
        this.endDate = endDate;
        this.signupDeadline = signupDeadline;
        this.club = club;
        this.teamRegistry = new TeamRegistry();
    }

    public void setTournamentFormatStrategy() {
        this.tournamentFormatStrategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(getTournamentFormat());
    }

    public Club getClub() {
        return club;
    }

    @JsonProperty("participants")
    public List<String> getParticipantUsernames() {
        List<String> usernames = new ArrayList<>();
        if (participants != null) {
            for (Player p : participants) {
                usernames.add(p.getUsername());
            }
        }
        return usernames;
    }

    public String getName() {
        return name;
    }

    public LocalDate estimateEndDate() {
        int days = this.tournamentFormatStrategy.estimateNeededDays(getTeamsNumber(), getCourtNumber());
        return this.startDate.plusDays(days);
    }

    public int availableSpots() {
        return getTeamsNumber() - getTeamRegistry().takenSpots();
    }

    public void reserveSpot(Player... players) {
        getTeamRegistry().reserveSpot(this, isSingles(), players);
    }

    public boolean availableSpot() {
        return availableSpots() > 0;
    }

    public boolean availableTeamSpot() {
        int spot = getTeamsNumber() - getTeamRegistry().getTotalTeams();
        return spot > 0;
    }

    private TeamRegistry getTeamRegistry() {
        return teamRegistry;
    }

    @JsonIgnore
    public List<Team> getConfirmedTeams() {
        return getTeamRegistry().getConfirmedTeams();
    }

    @JsonIgnore
    public List<Team> getPendingTeams() {
        return getTeamRegistry().getPendingTeams();
    }

    @JsonIgnore
    public List<Team> getPartialTeams() {
        return getTeamRegistry().getPartialTeams();
    }

    @JsonIgnore
    public boolean isSingles() {
        return getTournamentType().equals("Men's singles") || getTournamentType().equals("Women's singles");
    }

    @JsonIgnore
    public Team getReservedTeam(Player player) {
        return getTeamRegistry().getReservedTeam(player);
    }

    public void processInviteForSingles(Invite invite, Team team) {
        getTeamRegistry().processInviteForSingles(invite, team);
        if (inviteAccepted(invite)) addParticipants(team);
    }

    private boolean inviteAccepted(Invite invite) {
        return invite.getStatus()==InviteStatus.ACCEPTED;
    }

    public void addParticipants(Team team) {
        List<Player> players = team.getPlayers();
        for (Player player : players) {
            if (player != null) {
                addParticipant(player);
            }
        }
    }

    private void addParticipant(Player player) {
        if (participants == null) { participants = new ArrayList<>();}
        participants.add(player);
        notifySubscribers(this);
    }

    public void processInviteForDoubles(Invite invite, Team team, Invite teammateInvite) {
        getTeamRegistry().processInviteForDoubles(invite, team, teammateInvite);
        if (inviteAccepted(invite) || inviteAccepted(teammateInvite)) addParticipants(team);
    }

    public boolean playerAlreadyInATeam(Player player) {
        return getTeamRegistry().playerAlreadyInATeam(player);
    }

    public JoinTournamentView.JoinError addPlayer(Player player) {
        if (!availableSpot()) return JoinTournamentView.JoinError.NO_AVAILABLE_SPOTS;
        if (playerAlreadyInATeam(player)) return JoinTournamentView.JoinError.ALREADY_IN_A_TEAM;
        Player p = getTeamRegistry().addPlayer(player, isSingles(), this);
        if (p != null) addParticipant(p);
        return JoinTournamentView.JoinError.SUCCESS;
    }

    @Override
    public void subscribe(Subscriber host) {
        if (this.host == null) {
            this.host = host;
        }
    }

    @Override
    public void unsubscribe(Subscriber host) {
        if (this.host == host) {
            this.host = null;
        }
    }

    @Override
    public void notifySubscribers(Tournament tournament) {
        this.host.update(this.club, this);
    }

    @JsonIgnore
    public String getTournamentType() {
        return getTournamentRules().getTournamentType();
    }

    @JsonIgnore
    public String getMatchFormat() {
        return getTournamentRules().getMatchFormat();
    }

    @JsonIgnore
    public String getCourtType() {
        return getTournamentRules().getCourtType();
    }

    @JsonIgnore
    public int getCourtNumber() {
        return getTournamentRules().getCourtNumber();
    }

    @JsonIgnore
    public int getTeamsNumber() {
        return getTournamentRules().getTeamsNumber();
    }

    @JsonIgnore
    public String getTournamentFormat() {
        return getTournamentRules().getTournamentFormat();
    }

    public TournamentRules getTournamentRules() {
        return tournamentRules;
    }

    public List<Double> getPrizes() {
        return prizes;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public LocalDate getSignupDeadline() {
        return signupDeadline;
    }

    @JsonIgnore
    public double getJoinFee() {
        return getTournamentRules().getJoinFee();
    }

    @JsonIgnore
    public double getCourtPrice() {
        return getTournamentRules().getCourtPrice();
    }

    public List<Player> getParticipants() {
        return participants;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Tournament)) return false;
        Tournament to = (Tournament) o;
        if (this == to) return true;
        return Objects.equals(name, to.name) && Objects.equals(getTournamentFormat(), to.getTournamentFormat()) && Objects.equals(getTournamentType(), to.getTournamentType()) && Objects.equals(getStartDate(), to.getStartDate()) && Objects.equals(getClub(), to.getClub());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, getTournamentRules().getTournamentFormat(), getTournamentRules().getTournamentType(), getStartDate(), getClub());
    }

}
