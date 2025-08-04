package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.simonetagliaferri.model.domain.Tournament;

import java.io.IOException;

public class TournamentKeySerializer extends JsonSerializer<Tournament> {
    @Override
    public void serialize(Tournament tournament, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        TournamentKey key = TournamentKey.of(tournament);
        gen.writeFieldName(key.toString());
    }
}
