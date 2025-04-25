package it.simonetagliaferri.model.strategy;

public interface TournamentFormatStrategy {
    int estimateNeededDays(int teamsNumber, int courtNumber);
}
