package it.simonetagliaferri.model.strategy;

import java.io.Serializable;

public class SingleEliminationStrategy implements TournamentFormatStrategy, Serializable {
    private static final int MATCHES_A_DAY_PER_COURT = 4;

    @Override
    public int estimateNeededDays(int teams, int courts) {
        if (teams < 1 || courts < 1) throw new IllegalArgumentException();
        int t = teams;
        int days = 0;
        // Play full rounds until 4 teams remain, each round may span multiple days
        while (t > 4) {
            int matchesThisRound = t / 2;
            int perDay = Math.min(matchesThisRound, dailyCapacity(t, courts));
            days += ceilDiv(matchesThisRound, perDay);
            t = (t + 1) / 2; // winners advance
        }
        // Dedicated playoffs: one day for semifinals (if >=4), one day for final (if >=2)
        days += playoffDays(t);
        return days;
    }

    private int dailyCapacity(int teamsHere, int courts) {
        int courtCap = courts * MATCHES_A_DAY_PER_COURT;
        int teamCap = Math.max(1, teamsHere / 2);
        return Math.max(1, Math.min(courtCap, teamCap));
    }

    private int playoffDays(int teamsHere) {
        int d = 0;
        if (teamsHere >= 4) d += 1;
        if (teamsHere >= 2) d += 1;
        return d;
    }

    private int ceilDiv(int a, int b) {
        return ((a + b - 1) / b);
    }
}
