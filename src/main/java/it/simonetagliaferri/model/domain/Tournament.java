package it.simonetagliaferri.model.domain;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import it.simonetagliaferri.model.dao.fs.TournamentStrategyDeserializer;
import it.simonetagliaferri.model.dao.fs.TournamentStrategySerializer;
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
    private final String name;
    private final TournamentRules tournamentRules;
    private List<Double> prizes;
    private final LocalDate startDate;
    private LocalDate endDate;
    private LocalDate signupDeadline;
    @JsonIgnore
    private final Club club;
    @JsonSerialize(using = TournamentStrategySerializer.class)
    @JsonDeserialize(using = TournamentStrategyDeserializer.class)
    private TournamentFormatStrategy tournamentFormatStrategy;
    private TeamRegistry teamRegistry;
    @JsonIgnore
    private List<Player> participants;
    @JsonIgnore
    private List<Subscriber> subscribers;

    public Tournament(String tournamentName, String tournamentFormat, String tournamentType, LocalDate startDate, Club club) {
        this.name = tournamentName;
        this.tournamentRules = new TournamentRules(tournamentFormat, tournamentType);
        this.startDate = startDate;
        this.club = club;
    }

    public Tournament(String tournamentName, String tournamentType, String tournamentFormat, String matchFormat,
                      String courtType, int courtNumber, int teamsNumber, List<Double> prizes, LocalDate startDate,
                      LocalDate endDate, LocalDate signupDeadline, Club club, double joinFee, double courtPrice) {
        this.name = tournamentName;
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

    @JsonProperty("club")
    public String getClubName() {
        return club.getName();
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

    @JsonProperty("subscribers")
    public List<String> getSubscriberUsernames() {
        List<String> usernames = new ArrayList<>();
        if (subscribers != null) {
            for (Subscriber s : subscribers) {
                if (s instanceof User) {
                    usernames.add(((User) s).getUsername());
                } else {
                    usernames.add(s.toString()); // fallback for non-User subscribers
                }
            }
        }
        return usernames;
    }


    public String getTournamentName() {
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

    public List<Team> getConfirmedTeams() {
        return getTeamRegistry().getConfirmedTeams();
    }

    public List<Team> getPendingTeams() {
        return getTeamRegistry().getPendingTeams();
    }

    public List<Team> getPartialTeams() {
        return getTeamRegistry().getPartialTeams();
    }

    @JsonIgnore
    public boolean isSingles() {
        return getTournamentType().equals("Men's singles") || getTournamentType().equals("Women's singles");
    }

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
        if (subscribers == null) {
            subscribers = new ArrayList<>();
        }
        subscribers.add(host);
    }

    @Override
    public void unsubscribe(Subscriber host) {
        subscribers.remove(host);
    }

    @Override
    public void notifySubscribers(Tournament tournament) {
        for (Subscriber s : subscribers) {
            s.update(this.club, this); // Notify the host
        }
    }

    public String getTournamentType() {
        return getTournamentRules().getTournamentType();
    }

    public String getMatchFormat() {
        return getTournamentRules().getMatchFormat();
    }

    public String getCourtType() {
        return getTournamentRules().getCourtType();
    }

    public int getCourtNumber() {
        return getTournamentRules().getCourtNumber();
    }

    public int getTeamsNumber() {
        return getTournamentRules().getTeamsNumber();
    }

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

    public double getJoinFee() {
        return getTournamentRules().getJoinFee();
    }

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
