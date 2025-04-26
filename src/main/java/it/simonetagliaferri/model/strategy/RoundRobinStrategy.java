package it.simonetagliaferri.model.strategy;

public class RoundRobinStrategy implements TournamentFormatStrategy {
    private static final int MATCHES_A_DAY_PER_COURT = 3;

    @Override
    public int estimateNeededDays(int teamsNumber, int courtNumber) {
        int matchesADay = courtNumber * MATCHES_A_DAY_PER_COURT;
        int matchNum = teamsNumber*(teamsNumber-1)/2;
        int days;
        if (matchesADay<=teamsNumber) { // Every team plays a match a day.
            days=matchNum/matchesADay;
        }
        else {
            days=matchNum/teamsNumber;
        }
        return days;
    }
}
