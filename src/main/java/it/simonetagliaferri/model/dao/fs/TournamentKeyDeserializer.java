package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;
import it.simonetagliaferri.model.domain.Tournament;

public class TournamentKeyDeserializer extends KeyDeserializer {
    @Override
    public Tournament deserializeKey(String key, DeserializationContext context) {
        // your key string is like: name|format|type|date|clubKey
        String[] parts = key.split("\\|", 6);

        TournamentKey tk = new TournamentKey();
        tk.setName(parts[0]);
        tk.setFormat(parts[1]);
        tk.setType(parts[2]);
        tk.setStartDate(java.time.LocalDate.parse(parts[3]));

        // clubKey is serialized as "name|street|number|city|hostUsername"
        String[] clubParts = parts[4].split("\\|", 5);
        ClubKey ck = new ClubKey();
        ck.setName(clubParts[0]);
        ck.setStreet(clubParts[1]);
        ck.setNumber(clubParts[2]);
        ck.setCity(clubParts[3]);
        ck.setHostUsername(clubParts[4]);

        tk.setClubKey(ck);
        return tk.toTournament();
    }
}