package it.simonetagliaferri.model.strategy;

import java.io.Serializable;

public class RoundRobinStrategy implements TournamentFormatStrategy, Serializable {
    private static final int MATCHES_A_DAY_PER_COURT = 4;

    @Override
    public int estimateNeededDays(int teams, int courts) {
        if (teams < 1 || courts < 1) throw new IllegalArgumentException();
        int matches = (teams * (teams - 1)) / 2; // round robin total
        int capacity = dailyCapacity(teams, courts);
        int rrDays = ceilDiv(matches, capacity);
        int playoffs = playoffDays(teams); // +1 semis day if >=4, +1 final day if >=2
        return rrDays + playoffs;
    }

    private int dailyCapacity(int teams, int courts) {
        int courtCap = courts * MATCHES_A_DAY_PER_COURT;
        int teamCap  = Math.max(1, teams / 2); // disjoint pairings per day
        return Math.max(1, Math.min(courtCap, teamCap));
    }

    private int playoffDays(int teams) {
        int d = 0;
        if (teams >= 4) d += 1; // semifinals day
        if (teams >= 2) d += 1; // final day
        return d;
    }

    private int ceilDiv(int a, int b) {
        return ((a + b - 1) / b);
    }
}
