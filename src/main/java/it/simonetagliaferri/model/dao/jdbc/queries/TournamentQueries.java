package it.simonetagliaferri.model.dao.jdbc.queries;

public class TournamentQueries {
    private static final String SAVE_TOURNAMENT =
            "INSERT INTO tournaments (clubOwner, clubName, tournamentName, " +
                    "tournamentFormat, tournamentType, matchFormat, courtType, teamsNumber, courtNumber, " +
                    "joinFee, courtPrice, startDate, endDate, signupDeadline) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SAVE_PRIZE =
            "INSERT INTO prizes (place, clubOwner, clubName, tournamentName, value) " +
                    "VALUES (?, ?, ?, ?, ?)";

    private static final String SAVE_TEAM =
            "INSERT INTO teams (id, clubName, clubOwner, tournamentName, player1, player2, status) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE " +
                    "player1 = VALUES(player1), " +
                    "player2 = VALUES(player2), " +
                    "status  = VALUES(status)";

    private static final String GET_TOURNAMENT = "SELECT tournamentName, tournamentFormat, tournamentType, matchFormat, courtType, courtNumber," +
            "joinFee, courtPrice, startDate, signupDeadline, endDate, teamsNumber FROM tournaments WHERE clubOwner = ? AND clubName = ? AND tournamentName = ?";
    private static final String GET_PRIZES = "SELECT value FROM prizes WHERE clubOwner = ? AND clubName = ? AND tournamentName = ?";
    private static final String GET_TEAMS = "SELECT player1, player2, status FROM teams WHERE clubOwner = ? AND clubName = ? AND tournamentName = ? AND status = ?";
    private static final String GET_TOURNAMENT_NAMES = "SELECT tournamentName FROM tournaments WHERE clubOwner = ? AND clubName = ?";

    private static final String GET_TOURNAMENTS_BY_CITY = "SELECT DISTINCT clubs.clubName, clubOwner FROM tournaments JOIN clubs WHERE tournaments.clubOwner = clubs.owner " +
            "AND tournaments.clubName = clubs.clubName AND clubs.city = ?";

    private static final String GET_TOURNAMENTS_BY_PLAYER = "SELECT tournaments.clubName, tournaments.clubOwner, tournaments.tournamentName FROM tournaments JOIN teams " +
            "WHERE tournaments.tournamentName = teams.tournamentName AND tournaments.clubName = teams.clubName AND tournaments.clubOwner = " +
            "teams.clubOwner AND status = 'CONFIRMED' AND teams.player1 = ? OR teams.player2 = ?";

    private TournamentQueries() {
    }

    public static String saveTournament() {
        return SAVE_TOURNAMENT;
    }

    public static String savePrize() {
        return SAVE_PRIZE;
    }

    public static String saveTeam() {
        return SAVE_TEAM;
    }

    public static String getTournament() {
        return GET_TOURNAMENT;
    }

    public static String getPrizes() {
        return GET_PRIZES;
    }

    public static String getTeams() {
        return GET_TEAMS;
    }

    public static String getTournamentNames() {
        return GET_TOURNAMENT_NAMES;
    }

    public static String getTournamentsByCity() {
        return GET_TOURNAMENTS_BY_CITY;
    }

    public static String getTournamentsByPlayer() {
        return GET_TOURNAMENTS_BY_PLAYER;
    }
}
