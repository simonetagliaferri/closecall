package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.TournamentDAO;
import it.simonetagliaferri.model.domain.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JDBCTournamentDAO implements TournamentDAO {

    private static final String SAVE_TOURNAMENT =
            "INSERT INTO tournaments (clubOwner, clubName, tournamentName, " +
                    "tournamentFormat, tournamentType, matchFormat, courtType, teamsNumber, courtNumber, " +
                    "joinFee, courtPrice, startDate, endDate, signupDeadline) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "tournamentFormat = VALUES(tournamentFormat), " +
                    "tournamentType = VALUES(tournamentType), " +
                    "matchFormat = VALUES(matchFormat), " +
                    "courtType = VALUES(courtType), " +
                    "teamsNumber = VALUES(teamsNumber), " +
                    "courtNumber = VALUES(courtNumber), " +
                    "joinFee = VALUES(joinFee), " +
                    "courtPrice = VALUES(courtPrice), " +
                    "startDate = VALUES(startDate), " +
                    "endDate = VALUES(endDate), " +
                    "signupDeadline = VALUES(signupDeadline)";

    private static final String SAVE_PRIZE =
            "INSERT INTO prizes (place, clubOwner, clubName, tournamentName, value) " +
                    "VALUES (?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "value = VALUES(value)";

    private static final String SAVE_TEAM =
            "INSERT INTO teams (id, clubName, clubOwner, tournamentName, player1, player2, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "player1 = VALUES(player1), " +
                    "player2 = VALUES(player2), " +
                    "status  = VALUES(status)";

    private static final String GET_TOURNAMENT = "SELECT * FROM tournaments WHERE clubOwner = ? AND clubName = ? AND tournamentName = ?";
    private static final String GET_PRIZES = "SELECT value FROM prizes WHERE clubOwner = ? AND clubName = ? AND tournamentName = ?";
    private static final String GET_TEAMS = "SELECT player1, player2, status FROM teams WHERE clubOwner = ? AND clubName = ? AND tournamentName = ? AND status = ?";
    private static final String GET_TOURNAMENT_NAMES = "SELECT tournamentName FROM tournaments WHERE clubOwner = ? AND clubName = ?";

    private static final String GET_TOURNAMENTS_BY_CITY = "SELECT clubs.clubName, clubOwner FROM tournaments JOIN clubs WHERE tournaments.clubOwner = clubs.owner " +
            "AND tournaments.clubName = clubs.clubName AND clubs.city = ?";

    private static final String GET_TOURNAMENTS_BY_PLAYER = "SELECT tournaments.clubName, tournaments.clubOwner, tournaments.tournamentName FROM tournaments JOIN teams " +
            "WHERE tournaments.tournamentName = teams.tournamentName AND tournaments.clubName = teams.clubName AND tournaments.clubOwner = " +
            "teams.clubOwner AND teams.player1 = ? OR teams.player2 = ?";

    @Override
    public void saveTournament(Club club, Tournament tournament) {
        String clubName = club.getName();
        String clubOwner = club.getOwner().getUsername();
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
                PreparedStatement ps = conn.prepareStatement(SAVE_TOURNAMENT);
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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        savePrizes(club, tournament);
        saveTeams(club, tournament);
    }

    @Override
    public List<Tournament> getTournaments(Club club) {
        String clubName = club.getName();
        String clubOwner = club.getOwner().getUsername();
        List<Tournament> tournaments = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TOURNAMENT_NAMES);
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String name = rs.getString("tournamentName");
                    Tournament t = getTournament(club, name);
                    if (t != null) {
                        tournaments.add(t);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return tournaments;
    }

    @Override
    public Tournament getTournament(Club club, String tournamentName) {
        String clubName = club.getName();
        String clubOwner = club.getOwner().getUsername();
        Tournament tournament = null;
        TournamentRules tournamentRules;
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TOURNAMENT);
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);
            ps.setString(3, tournamentName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                tournament = new Tournament(rs.getString("tournamentName"));
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tournament> getTournamentsByCity(String city) {
        List <Tournament> tournaments = new ArrayList<>();
        List<Club> clubs = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TOURNAMENTS_BY_CITY);
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
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Tournament> getPlayerTournaments(Player player) {
        List <Tournament> tournaments = new ArrayList<>();
        String playerName = player.getUsername();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TOURNAMENTS_BY_PLAYER);
            ps.setString(1, playerName);
            ps.setString(2, playerName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String clubName = rs.getString("clubName");
                String clubOwner = rs.getString("clubOwner");
                String tournamentName = rs.getString("tournamentName");
                Host host = new Host(clubOwner);
                Club club = new Club(clubName, host);
                tournaments.add(getTournament(club, tournamentName));
            }
            return tournaments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void savePrizes(Club club, Tournament tournament) {
        List<Double> prizes = tournament.getPrizes();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_PRIZE);
            for (int i = 0; i < prizes.size(); i++) {
                double prize = prizes.get(i);
                ps.setInt(1, i);
                ps.setString(2, club.getOwner().getUsername());
                ps.setString(3, club.getName());
                ps.setString(4, tournament.getName());
                ps.setDouble(5, prize);
                ps.addBatch();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private List<Double> getPrizes(Club club, String tournamentName) {
        String clubName = club.getName();
        String clubOwner = club.getOwner().getUsername();
        List<Double> prizes = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_PRIZES);
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);
            ps.setString(3, tournamentName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                prizes.add(rs.getDouble("value"));
            }
            return prizes;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private TeamRegistry getTeamRegistry(Club club, Tournament tournament) {
        String clubName = club.getName();
        String clubOwner = club.getOwner().getUsername();
        List<Team> confirmedTeams = new ArrayList<>();
        List<Team> pendingTeams = new ArrayList<>();
        List<Team> partialTeams = new ArrayList<>();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(GET_TEAMS);
            ps.setString(1, clubOwner);
            ps.setString(2, clubName);
            ps.setString(3, tournament.getName());
            ps.setString(4, TeamStatus.CONFIRMED.name());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Team team = getTeam(rs.getString("player1"), rs.getString("player2"),
                        TeamStatus.valueOf(rs.getString("status")), tournament);
                confirmedTeams.add(team);
            }
            PreparedStatement ps2 = conn.prepareStatement(GET_TEAMS);
            ps2.setString(1, clubOwner);
            ps2.setString(2, clubName);
            ps2.setString(3, tournament.getName());
            ps2.setString(4, TeamStatus.PENDING.name());
            ResultSet rs2 = ps2.executeQuery();
            while (rs2.next()) {
                Team team = getTeam(rs2.getString("player1"), rs2.getString("player2"),
                        TeamStatus.valueOf(rs2.getString("status")), tournament);
                pendingTeams.add(team);
            }
            PreparedStatement ps3 = conn.prepareStatement(GET_TEAMS);
            ps3.setString(1, clubOwner);
            ps3.setString(2, clubName);
            ps3.setString(3, tournament.getName());
            ps3.setString(4, TeamStatus.PARTIAL.name());
            ResultSet rs3 = ps3.executeQuery();
            while (rs3.next()) {
                Team team = getTeam(rs3.getString("player1"), rs3.getString("player2"),
                        TeamStatus.valueOf(rs3.getString("status")), tournament);
                partialTeams.add(team);
            }
            PreparedStatement ps4 = conn.prepareStatement(GET_TEAMS);
            ps4.setString(1, clubOwner);
            ps4.setString(2, clubName);
            ps4.setString(3, tournament.getName());
            ps4.setString(4, TeamStatus.PENDING_PARTIAL.name());
            ResultSet rs4 = ps4.executeQuery();
            while (rs4.next()) {
                Team team = getTeam(rs4.getString("player1"), rs4.getString("player2"),
                        TeamStatus.valueOf(rs4.getString("status")), tournament);
                partialTeams.add(team);
                pendingTeams.add(team);
            }
            return new TeamRegistry(confirmedTeams, pendingTeams, partialTeams);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Team getTeam(String p1, String p2, TeamStatus status, Tournament tournament) {
        Player player1 = new Player(p1);
        Player player2 = null;
        if (p2 != null) {
            player2 = new Player(p2);
        }
        Team team = new Team(player1, player2, tournament);
        team.setStatus(status);
        return team;
    }

    private void saveTeams(Club club, Tournament tournament) {
        List<Team> confirmedTeams = tournament.getConfirmedTeams();
        List<Team> partialTeams = tournament.getPartialTeams();
        List<Team> pendingTeams = tournament.getPendingTeams();
        try {
            Connection conn = ConnectionFactory.getConnection();
            PreparedStatement ps = conn.prepareStatement(SAVE_TEAM);
            prepareTeam(club, tournament, confirmedTeams, ps);
            prepareTeam(club, tournament, partialTeams, ps);
            prepareTeam(club, tournament, pendingTeams, ps);
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void prepareTeam(Club club, Tournament tournament, List<Team> teamList, PreparedStatement ps) throws SQLException {
        for (int i = 0; i < teamList.size(); i++) {
            Team team = teamList.get(i);
            ps.setInt(1, i);
            ps.setString(2, club.getName());
            ps.setString(3, club.getOwner().getUsername());
            ps.setString(4, tournament.getName());
            if (team.getPlayer1() != null) {
                ps.setString(5, team.getPlayer1().getUsername());
            } else {
                ps.setNull(5, Types.VARCHAR);
            }
            if (team.getPlayer2() != null) {
                ps.setString(6, team.getPlayer2().getUsername());
            } else {
                ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, team.getStatus().name());
            ps.addBatch();
        }
    }

}
