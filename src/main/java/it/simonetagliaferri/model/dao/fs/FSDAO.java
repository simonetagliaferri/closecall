package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;

public abstract class FSDAO {
    protected final File file;
    protected final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    private long lastModified = 0;

    protected FSDAO(String fileName) {
        this.file = new File(fileName);
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
