package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.model.invite.Invite;
import it.simonetagliaferri.model.invite.InviteStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TeamRegistry implements Serializable {

    private int teamsNumber;
    private final List<Team> confirmedTeams;
    private final List<Team> pendingTeams;
    private final List<Team> partialTeams;

    public TeamRegistry(int teamsNumber) {
        this.confirmedTeams = new ArrayList<>();
        this.pendingTeams = new ArrayList<>();
        this.partialTeams = new ArrayList<>();
        this.teamsNumber = teamsNumber;
    }

    public TeamRegistry(List<Team> confirmedTeams, List<Team> pendingTeams, List<Team> partialTeams) {
        this.confirmedTeams = confirmedTeams;
        this.pendingTeams = pendingTeams;
        this.partialTeams = partialTeams;
    }

    public List<Team> getConfirmedTeams() {
        return confirmedTeams;
    }
    public List<Team> getPendingTeams() {
        return pendingTeams;
    }
    public List<Team> getPartialTeams() {
        return partialTeams;
    }

    public int getTotalTeams() {
        return confirmedTeams.size() + pendingTeams.size() + partialTeams.size();
    }

    public int takenSpots() {
        return confirmedTeams.size() + pendingTeams.size() + partialTeams.size()/2;
    }

    public void reserveSpot(Tournament tournament, boolean isSingles, Player... players) {
        Team team;
        if (players.length == 1) {
            team = new Team(players[0], getTeamType(isSingles), tournament);
            this.pendingTeams.add(team);
            if (!isSingles) {
                team.setStatus(TeamStatus.PENDING_PARTIAL);
                this.partialTeams.add(team);
            } else {
                team.setStatus(TeamStatus.PENDING);
            }
        } else if (players.length == 2) {
            team = new Team(players[0], players[1], tournament);
            team.setStatus(TeamStatus.PENDING);
            this.pendingTeams.add(team);
        } else {
            throw new IllegalArgumentException("A team must have either 1 or 2 players.");
        }
    }

    private TeamType getTeamType(boolean isSingles) {
        if (isSingles) {
            return TeamType.SINGLE;
        } else return TeamType.DOUBLE;
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

    public void processInviteForSingles(Invite invite, Team team) {
        if (!pendingTeams.contains(team)) return;
        switch (invite.getStatus()) {
            case ACCEPTED:
                team.setStatus(TeamStatus.CONFIRMED);
                pendingTeams.remove(team);
                confirmedTeams.add(team);
                break;
            case DECLINED:
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
                team.setStatus(TeamStatus.CONFIRMED);
                confirmedTeams.add(team);
            } else {
                team.setStatus(TeamStatus.PARTIAL);
                partialTeams.add(team);
            }
        } else if (s2 == null && (s1 == InviteStatus.DECLINED || s1 == InviteStatus.EXPIRED)) {
            pendingTeams.remove(team);
            if (team.isFull()) {
                team.removePlayer(invite.getPlayer());
                team.setStatus(TeamStatus.PARTIAL);
                partialTeams.add(team);
            }
        } else if (s1 == InviteStatus.DECLINED || s1 == InviteStatus.EXPIRED ||
                s2 == InviteStatus.DECLINED || s2 == InviteStatus.EXPIRED) {
            pendingTeams.remove(team);
        } else if (s1 == InviteStatus.ACCEPTED && s2 == InviteStatus.ACCEPTED) {
            team.setStatus(TeamStatus.CONFIRMED);
            pendingTeams.remove(team);
            confirmedTeams.add(team);
        }
    }

    public boolean playerAlreadyInATeam(Player player) {
        for (Team team : this.confirmedTeams) {
            if (team.hasPlayer(player)) {
                return true;
            }
        }
        for (Team team : this.pendingTeams) {
            if (team.hasPlayer(player)) {
                return true;
            }
        }
        for (Team team : this.partialTeams) {
            if (team.hasPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    public Player addPlayer(Player player, boolean isSingles, Tournament tournament) {
        Player p = null;
        if (isSingles) {
            Team team = new Team(player, getTeamType(true), tournament);
            team.setStatus(TeamStatus.CONFIRMED);
            this.confirmedTeams.add(team);
            p = player;
        } else {
            for (Team team : this.partialTeams) {
                if (!team.isFull()) {
                    team.addPlayer(player);
                    p = player;
                    if (this.pendingTeams.contains(team)) {
                        team.setStatus(TeamStatus.PENDING);
                        this.partialTeams.remove(team);
                    } else {
                        team.setStatus(TeamStatus.CONFIRMED);
                        this.confirmedTeams.add(team);
                    }
                    break;
                }
            }
        }
        return p;
    }

    public void setTeamsNumber(int teamsNumber) {
        this.teamsNumber = teamsNumber;
    }

    public int getTeamsNumber() {
        return teamsNumber;
    }
}
