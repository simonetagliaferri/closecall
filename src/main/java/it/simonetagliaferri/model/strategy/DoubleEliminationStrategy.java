package it.simonetagliaferri.model.strategy;

public class DoubleEliminationStrategy implements TournamentFormatStrategy{
    private final static int MATCHES_A_DAY_PER_COURT = 3;

    @Override
    public int estimateNeededDays(int teamsNumber, int courtNumber) {
        int matchesADay = courtNumber * MATCHES_A_DAY_PER_COURT;
        int rounds = ((Double)(Math.ceil(Math.log(teamsNumber)/Math.log(2)))).intValue();
        int matchNum = (teamsNumber*2)-1;
        int matchNumNoFinals = matchNum-2;
        int days;
        if (matchesADay<=teamsNumber) { // Every team plays a match a day.
            days=(matchNumNoFinals/(matchesADay/rounds))+2;
        }
        else {
            days=(matchNumNoFinals/(teamsNumber/rounds))+2;
        }
        return days;
    }
}
