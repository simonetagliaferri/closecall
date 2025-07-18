package it.simonetagliaferri.model.strategy;

public class TournamentFormatStrategyFactory {
    private TournamentFormatStrategyFactory() {}

    public static TournamentFormatStrategy createTournamentFormatStrategy(String tournamentFormat) {
        switch(TournamentFormats.fromString(tournamentFormat)) {
            case ROUND_ROBIN:
                return new RoundRobinStrategy();
            case SINGLE_ELIMINATION:
                return new SingleEliminationStrategy();
            case DOUBLE_ELIMINATION:
                return new DoubleEliminationStrategy();
            default:
                throw new IllegalArgumentException("Invalid tournament type: " + tournamentFormat);
        }
    }
}
