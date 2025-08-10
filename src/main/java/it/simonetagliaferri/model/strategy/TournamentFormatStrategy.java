package it.simonetagliaferri.model.strategy;

import java.io.Serializable;

public interface TournamentFormatStrategy extends Serializable {
    int estimateNeededDays(int teamsNumber, int courtNumber);
}
