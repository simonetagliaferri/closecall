package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import it.simonetagliaferri.model.domain.Club;

import java.io.IOException;

public class ClubKeySerializer extends JsonSerializer<Club> {
    @Override
    public void serialize(Club club, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Represent the key as a ClubKey string
        ClubKey key = ClubKey.of(club);
        gen.writeFieldName(key.toString());
    }
}

