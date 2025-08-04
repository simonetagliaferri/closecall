package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import it.simonetagliaferri.model.strategy.DoubleEliminationStrategy;
import it.simonetagliaferri.model.strategy.RoundRobinStrategy;
import it.simonetagliaferri.model.strategy.SingleEliminationStrategy;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;

import java.io.IOException;

public class TournamentStrategyDeserializer extends JsonDeserializer<TournamentFormatStrategy> {
    @Override
    public TournamentFormatStrategy deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String type = p.getText();
        TournamentFormatStrategy tournamentFormatStrategy;
        switch (type) {
            case "RoundRobinStrategy":
                tournamentFormatStrategy = new RoundRobinStrategy();
                break;
            case "SingleEliminationStrategy":
                tournamentFormatStrategy = new SingleEliminationStrategy();
                break;
            case "DoubleEliminationStrategy":
                tournamentFormatStrategy = new DoubleEliminationStrategy();
                break;
            default:
                tournamentFormatStrategy = null;
        }
        return tournamentFormatStrategy;
    }
}

