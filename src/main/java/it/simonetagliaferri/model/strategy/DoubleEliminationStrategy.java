package it.simonetagliaferri.model.strategy;

import java.io.Serializable;

public class DoubleEliminationStrategy implements TournamentFormatStrategy, Serializable {
    private static final int MATCHES_A_DAY_PER_COURT = 4;

    @Override
    public int estimateNeededDays(int teams, int courts) {
        if (teams < 1 || courts < 1) throw new IllegalArgumentException();

        // Worst-case total matches: 2N - 1 (undefeated finalist loses first final)
        int totalMatches = 2 * teams - 1;

        // Put semis (2 matches) on a dedicated day and the final (1 match) on another day, when applicable.
        int playoffMatches = playoffMatches(teams);
        int bulkMatches = Math.max(0, totalMatches - playoffMatches);

        int capacity = dailyCapacity(teams, courts); // use initial teams for cap
        int bulkDays = ceilDiv(bulkMatches, capacity);

        return bulkDays + playoffDays(teams);
    }

    private int dailyCapacity(int teamsHere, int courts) {
        int courtCap = courts * MATCHES_A_DAY_PER_COURT;
        int teamCap = Math.max(1, teamsHere / 2);
        return Math.max(1, Math.min(courtCap, teamCap));
    }

    private int playoffDays(int teamsHere) {
        int d = 0;
        if (teamsHere >= 4) d += 1; // all semifinals on one day
        if (teamsHere >= 2) d += 1; // final on another day
        return d;
    }

    private int playoffMatches(int teamsHere) {
        int m = 0;
        if (teamsHere >= 4) m += 2; // semifinals
        if (teamsHere >= 2) m += 1; // final
        return m;
    }

    private int ceilDiv(int a, int b) {
        return ((a + b - 1) / b);
    }
}
