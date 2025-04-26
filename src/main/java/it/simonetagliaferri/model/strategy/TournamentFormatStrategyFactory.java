package it.simonetagliaferri.model.strategy;

public class TournamentFormatStrategyFactory {
    private TournamentFormatStrategyFactory() {}

    public static TournamentFormatStrategy createTournamentFormatStrategy(String tournamentType) {
        switch(tournamentType) {
            case "RoundRobin":
                return new RoundRobinStrategy();
            case "Single-elimination":
                return new SingleEliminationStrategy();
            case "Double-elimination":
                return new DoubleEliminationStrategy();
            default:
                throw new IllegalArgumentException("Invalid tournament type: " + tournamentType);
        }
    }
}
