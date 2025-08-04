package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.model.domain.Tournament;
import it.simonetagliaferri.model.strategy.TournamentFormatStrategy;

import java.io.File;

public abstract class FSDAO {
    protected final File file;
    protected final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private long lastModified = 0;

    protected FSDAO(String fileName) {
        this.file = new File(fileName);
        SimpleModule module = new SimpleModule();
        module.addKeySerializer(Club.class, new ClubKeySerializer());
        module.addKeyDeserializer(Club.class, new ClubKeyDeserializer());
        module.addKeySerializer(Tournament.class, new TournamentKeySerializer());
        module.addKeyDeserializer(Tournament.class, new TournamentKeyDeserializer());
        module.addSerializer(TournamentFormatStrategy.class, new TournamentStrategySerializer());
        module.addDeserializer(TournamentFormatStrategy.class, new TournamentStrategyDeserializer());
        mapper.registerModule(module);
    }

    protected void reloadIfChanged() {
        long modified = file.lastModified();
        if (modified > lastModified) {
            loadFromFile();
            lastModified = modified;
        }
    }

    protected abstract void loadFromFile();

    protected void updateLastModified() {
        lastModified = file.lastModified();
    }

}
