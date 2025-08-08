package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;
import it.simonetagliaferri.model.observer.Publisher;
import it.simonetagliaferri.model.observer.Subscriber;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategyFactory;
import it.simonetagliaferri.view.cli.JoinTournamentView;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Tournament implements Publisher, Serializable {
    private final String name;
    private TournamentRules tournamentRules;
    private List<Double> prizes;
    private Club club;
    private TournamentFormatStrategy tournamentFormatStrategy;
    private TeamRegistry teamRegistry;
    private List<Player> participants;
    private Subscriber host;

    public Subscriber getHost() {
        return host;
    }

    public void setHost(Subscriber host) {
        this.host = host;
    }

    public Tournament(String name) {
        this.name = name;
    }

    public Tournament(String name, String tournamentFormat, String tournamentType, LocalDate startDate) {
        this.name = name;
        this.tournamentRules = new TournamentRules(tournamentFormat, tournamentType, startDate);
        setTournamentFormatStrategy();
    }

    public Tournament(String name, String tournamentFormat, String tournamentType, LocalDate startDate, Club club) {
        this.name = name;
        this.tournamentRules = new TournamentRules(tournamentFormat, tournamentType, startDate);
        this.club = club;
        setTournamentFormatStrategy();
    }

    public void setTournamentRules(String tournamentFormat, String tournamentType, String matchFormat, String courtType, int courtNumber, int teamsNumber) {
        if (this.tournamentRules == null) {
            this.tournamentRules = new TournamentRules(tournamentFormat, tournamentType, matchFormat, courtType, courtNumber);
        }
        if (this.teamRegistry == null) {
            this.teamRegistry = new TeamRegistry(teamsNumber);
        }
        setTournamentFormatStrategy();
    }

    public void setTournamentCosts(double joinFee, double courtPrice) {
        this.tournamentRules.setTournamentCosts(joinFee, courtPrice);
    }

    public void setTournamentDates(LocalDate startDate, LocalDate signupDeadline, LocalDate endDate) {
        this.tournamentRules.setTournamentDates(startDate, signupDeadline, endDate);
    }

    public void setPrizes(List<Double> prizes) {
        this.prizes = prizes;
    }


    public void setClub(Club club) {
        this.club = club;
    }

    public void setTeamsNumber(int teamsNumber) {
        getTeamRegistry().setTeamsNumber(teamsNumber);
    }

    private void setTournamentFormatStrategy() {
        this.tournamentFormatStrategy = TournamentFormatStrategyFactory.createTournamentFormatStrategy(getTournamentFormat());
    }

    public void setTournamentRules(TournamentRules tournamentRules) {
        this.tournamentRules = tournamentRules;
    }

    public void setTeamRegistry(TeamRegistry teamRegistry) {
        this.teamRegistry = teamRegistry;
    }

    public Club getClub() {
        return club;
    }

    public String getName() {
        return name;
    }

    public LocalDate estimateEndDate() {
        int days = this.tournamentFormatStrategy.estimateNeededDays(getTeamsNumber(), getCourtNumber());
        return getStartDate().plusDays(days);
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

    public List<Team> getConfirmedTeams() {
        if (getTeamRegistry() != null) {
            return getTeamRegistry().getConfirmedTeams();
        }
        return null;
    }

    public List<Team> getPendingTeams() {
        if (getTeamRegistry() != null) {
            return getTeamRegistry().getPendingTeams();
        }
        return null;
    }

    public List<Team> getPartialTeams() {
        if (getTeamRegistry() != null) {
            return getTeamRegistry().getPartialTeams();
        }
        return null;
    }

    public TeamType getTeamType() {
        if (isSingles()) {
            return TeamType.SINGLE;
        } else return TeamType.DOUBLE;
    }

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

    public boolean isSameAs(Tournament other) {
        if (other == null) return false;
        return this.name.equals(other.getName()) && this.club.isSameAs(other.getClub());
    }


    @Override
    public void subscribe(Subscriber host) {
        this.host = host;
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
        return getTeamRegistry().getTeamsNumber();
    }

    public String getTournamentFormat() {
        return getTournamentRules().getTournamentFormat();
    }

    private TournamentRules getTournamentRules() {
        return tournamentRules;
    }

    public List<Double> getPrizes() {
        return prizes;
    }

    public LocalDate getStartDate() {
        return getTournamentRules().getStartDate();
    }

    public LocalDate getEndDate() {
        return getTournamentRules().getEndDate();
    }

    public LocalDate getSignupDeadline() {
        return getTournamentRules().getSignupDeadline();
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

    public TeamRegistry getTeamRegistry() {
        return teamRegistry;
    }

    public LocalDate minInviteExpiryDate() {
        return LocalDate.now();
    }

    public LocalDate maxInviteExpiryDate() {
        return getSignupDeadline();
    }

    public boolean isInviteExpireDateValid(LocalDate date) {
        return !date.isAfter(getSignupDeadline());
    }

}
