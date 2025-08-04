package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;

import java.io.IOException;

public class TournamentStrategySerializer extends JsonSerializer<TournamentFormatStrategy> {
    @Override
    public void serialize(TournamentFormatStrategy value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Serialize only the type name (like "RoundRobin")
        if (value != null) {
            gen.writeString(value.getClass().getSimpleName());
        } else {
            gen.writeNull();
        }
    }
}

