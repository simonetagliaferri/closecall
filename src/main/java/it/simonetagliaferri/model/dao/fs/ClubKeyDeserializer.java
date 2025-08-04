package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Host;

public class ClubKeyDeserializer extends KeyDeserializer {
    @Override
    public Club deserializeKey(String key, DeserializationContext ctxt) {
        String[] parts = key.split("\\|");
        if (parts.length != 5) return null;
        return new Club(parts[0], parts[1], parts[2], parts[3], new Host(parts[4]));
    }
}

