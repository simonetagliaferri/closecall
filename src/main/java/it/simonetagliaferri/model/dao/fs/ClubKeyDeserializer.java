package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

public class ClubKeyDeserializer extends KeyDeserializer {
    @Override
    public Object deserializeKey(String key, DeserializationContext context) {
        // Split by your delimiter
        String[] parts = key.split("\\|");
        ClubKey ck = new ClubKey();
        ck.setName(parts[0]);
        ck.setStreet(parts[1]);
        ck.setNumber(parts[2]);
        ck.setCity(parts[3]);
        ck.setHostUsername(parts[4]);
        return ck.toClub();
    }
}

