package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.dao.jdbc.queries.TournamentQueries;
import it.simonetagliaferri.model.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCTournamentDAO implements TournamentDAO {

    private static final String TOURNAMENT_NAME = "tournamentName";
    private static final String PLAYER_1 = "player1";
    private static final String PLAYER_2 = "player2";
    private static final String STATUS = "status";

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        String clubName = club.getName();
        String clubOwner = club.getOwnerUsername();
        String tournamentName = tournament.getName();
        String tournamentFormat = tournament.getTournamentFormat();
        String tournamentType = tournament.getTournamentType();
        String matchFormat = tournament.getMatchFormat();
        String courtType = tournament.getCourtType();
        int teamsNumber = tournament.getTeamsNumber();
        int courtNumber = tournament.getCourtNumber();
        double joinFee = tournament.getJoinFee();
        double courtPrice = tournament.getCourtPrice();
        LocalDate startDate = tournament.getStartDate();
        LocalDate endDate = tournament.getEndDate();
        LocalDate signupDeadline = tournament.getSignupDeadline();
        try {
            if (getTournament(club, tournamentName) == null) {
                Connection conn = ConnectionFactory.getConnection();
                PreparedStatement ps = conn.prepareStatement(TournamentQueries.saveTournament());
                ps.setString(1, clubOwner);
                ps.setString(2, clubName);
                ps.setString(3, tournamentName);
                ps.setString(4, tournamentFormat);
                ps.setString(5, tournamentType);
                ps.setString(6, matchFormat);
                ps.setString(7, courtType);
                ps.setInt(8, teamsNumber);
                ps.setInt(9, courtNumber);
                ps.setDouble(10, joinFee);
                ps.setDouble(11, courtPrice);
                ps.setDate(12, Date.valueOf(startDate));
                ps.setDate(13, Date.valueOf(endDate));
                ps.setDate(14, Date.valueOf(signupDeadline));
                ps.executeUpdate();
                savePrizes(club, tournament);
            }
        } catch (SQLException e) {
            throw new DAOException("Error while saving the tournament: " + e.getMessage());
        }
        saveTeams(club, tournament);
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        String clubName = club.getName();
        String clubOwner = club.getOwnerUsername();
        List<Tournament> tournaments = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.getTournamentNames());
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString(TOURNAMENT_NAME);
                    Tournament t = getTournament(club, name);
                    if (t != null) {
                        tournaments.add(t);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DAOException("Error while fetching club's tournaments: " + e.getMessage());
        }
        return tournaments;
    }

    @Override
    public Tournament getTournament(Club club, String tournamentName) {
        String clubName = club.getName();
        String clubOwner = club.getOwnerUsername();
        Tournament tournament = null;
        TournamentRules tournamentRules;
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.getTournament());
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);
            ps.setString(3, tournamentName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tournament = new Tournament(rs.getString(TOURNAMENT_NAME));
                tournament.setClub(club);
                tournament.setHost(club.getOwner());
                tournamentRules = new TournamentRules(
                        rs.getString("tournamentFormat"),
                        rs.getString("tournamentType"),
                        rs.getString("matchFormat"),
                        rs.getString("courtType"),
                        rs.getInt("courtNumber")
                );
                tournamentRules.setTournamentCosts(rs.getDouble("joinFee"),
                        rs.getDouble("courtPrice"));
                tournamentRules.setTournamentDates(rs.getDate("startDate").toLocalDate(), rs.getDate("signupDeadline").toLocalDate(),
                        rs.getDate("endDate").toLocalDate());
                tournament.setTournamentRules(tournamentRules);
                tournament.setPrizes(getPrizes(club, tournamentName));
                tournament.setTeamRegistry(getTeamRegistry(club, tournament));
                tournament.setTeamsNumber(rs.getInt("teamsNumber"));
            }
            return tournament;
        } catch (SQLException e) {
            throw new DAOException("Error while fetching the tournament: " + e.getMessage());
        }
    }

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        List<Tournament> tournaments = new ArrayList<>();
        List<Club> clubs = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.getTournamentsByCity());
            ps.setString(1, city);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String clubOwner = rs.getString("clubOwner");
                Host host = new Host(clubOwner);
                clubs.add(new Club(clubName, host));
            }
            for (Club club : clubs) {
                tournaments.addAll(getTournaments(club));
            }
            return tournaments;
        } catch (SQLException e) {
            throw new DAOException("Error while fetching tournaments: " + e.getMessage());
        }
    }

    @Override
    public List<Tournament> getPlayerTournaments(Player player) {
        List<Tournament> tournaments = new ArrayList<>();
        String playerName = player.getUsername();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.getTournamentsByPlayer());
            ps.setString(1, playerName);
            ps.setString(2, playerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String clubOwner = rs.getString("clubOwner");
                String tournamentName = rs.getString(TOURNAMENT_NAME);
                Host host = new Host(clubOwner);
                Club club = new Club(clubName, host);
                tournaments.add(getTournament(club, tournamentName));
            }
            return tournaments;
        } catch (SQLException e) {
            throw new DAOException("Error while fetching the player's tournaments: " + e.getMessage());
        }
    }

    private void savePrizes(Club club, Tournament tournament) {
        List<Double> prizes = tournament.getPrizes();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.savePrize());
            for (int i = 0; i < prizes.size(); i++) {
                double prize = prizes.get(i);
                ps.setInt(1, i);
                ps.setString(2, club.getOwnerUsername());
                ps.setString(3, club.getName());
                ps.setString(4, tournament.getName());
                ps.setDouble(5, prize);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error while saving the tournament's prizes: " + e.getMessage());
        }

    }

    private List<Double> getPrizes(Club club, String tournamentName) {
        String clubName = club.getName();
        String clubOwner = club.getOwnerUsername();
        List<Double> prizes = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.getPrizes());
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);
            ps.setString(3, tournamentName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prizes.add(rs.getDouble("value"));
            }
            return prizes;
        } catch (SQLException e) {
            throw new DAOException("Error while fetching the tournament's prizes: " + e.getMessage());
        }
    }

    private TeamRegistry getTeamRegistry(Club club, Tournament tournament) {
        String clubName = club.getName();
        String clubOwner = club.getOwnerUsername();
        List<Team> confirmedTeams;
        List<Team> pendingTeams;
        List<Team> partialTeams;
        try {
            Connection conn = ConnectionFactory.getConnection();
            confirmedTeams = getTeams(conn, clubOwner, clubName, tournament, TeamStatus.CONFIRMED.name());
            pendingTeams = getTeams(conn, clubOwner, clubName, tournament, TeamStatus.PENDING.name());
            partialTeams = getTeams(conn, clubOwner, clubName, tournament, TeamStatus.PARTIAL.name());
            pendingTeams.addAll(getTeams(conn, clubOwner, clubName, tournament, TeamStatus.PENDING_PARTIAL.name()));
            partialTeams.addAll(getTeams(conn, clubOwner, clubName, tournament, TeamStatus.PENDING_PARTIAL.name()));
            return new TeamRegistry(confirmedTeams, pendingTeams, partialTeams);
        } catch (SQLException e) {
            throw new DAOException("Error while fetching the tournament's teams: " + e.getMessage());
        }
    }

    private List<Team> getTeams(Connection conn, String clubOwner, String clubName, Tournament tournament, String teamStatus) throws SQLException {
        List<Team> teams = new ArrayList<>();
        PreparedStatement ps = conn.prepareStatement(TournamentQueries.getTeams());
        ps.setString(1, clubOwner);
        ps.setString(2, clubName);
        ps.setString(3, tournament.getName());
        ps.setString(4, teamStatus);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Team team = getTeam(rs.getString(PLAYER_1), rs.getString(PLAYER_2),
                    TeamStatus.valueOf(rs.getString(STATUS)), tournament);
            teams.add(team);
        }
        return teams;
    }

    private Team getTeam(String p1, String p2, TeamStatus status, Tournament tournament) {
        Player player1 = new Player(p1);
        Player player2;
        Team team;
        if (p2 != null) {
            player2 = new Player(p2);
            team = new Team(player1, player2, tournament);
        } else {
            TeamType teamType = tournament.isSingles() ? TeamType.SINGLE : TeamType.DOUBLE;
            team = new Team(player1, teamType, tournament);
        }
        team.setStatus(status);
        return team;
    }

    private void saveTeams(Club club, Tournament tournament) {
        List<Team> confirmedTeams = tournament.getConfirmedTeams();
        List<Team> partialTeams = tournament.getPartialTeams();
        List<Team> pendingTeams = tournament.getPendingTeams();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(TournamentQueries.saveTeam());
            prepareTeams(club, tournament, confirmedTeams, ps);
            prepareTeams(club, tournament, pendingTeams, ps);
            preparePartialTeams(club, tournament, partialTeams, ps);
            ps.executeBatch();
        } catch (SQLException e) {
            throw new DAOException("Error while saving the tournament's teams: " + e.getMessage());
        }
    }

    private void prepareTeams(Club club, Tournament tournament, List<Team> teamList, PreparedStatement ps) throws SQLException {
        ps.setString(2, club.getName());
        ps.setString(3, club.getOwnerUsername());
        ps.setString(4, tournament.getName());
        for (int i = 0; i < teamList.size(); i++) {
            prepareTeam(teamList, ps, i);
        }
    }

    private void preparePartialTeams(Club club, Tournament tournament, List<Team> teamList, PreparedStatement ps) throws SQLException {
        ps.setString(2, club.getName());
        ps.setString(3, club.getOwnerUsername());
        ps.setString(4, tournament.getName());
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).getStatus() != TeamStatus.PENDING_PARTIAL) {
                prepareTeam(teamList, ps, i);
            }
        }
    }

    private void prepareTeam(List<Team> teamList, PreparedStatement ps, int i) throws SQLException {
        Team team = teamList.get(i);
        ps.setInt(1, i);
        ps.setObject(5, team.getPlayer1() != null ? team.getPlayer1().getUsername() : null,
                Types.VARCHAR);
        ps.setObject(6, team.getPlayer2() != null ? team.getPlayer2().getUsername() : null,
                Types.VARCHAR);
        ps.setString(7, team.getStatus().name());
        ps.addBatch();
    }

}
