package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

abstract class FSDAO {

    private final Path file;

    protected FSDAO(Path baseDir, String fileName) {
        try {
            Files.createDirectories(baseDir);
        } catch (IOException e) {
            throw new DAOException("Cannot create base dir " + baseDir, e);
        }
        this.file = baseDir.resolve(fileName);   // <-- use the directory
        ensureFileExists();
    }

    protected void ensureFileExists() {
        try {
            if (Files.notExists(file)) Files.createFile(file);
        } catch (IOException e) {
            throw new DAOException("Cannot create data file " + file, e);
        }
    }

    protected Path file() {
        return file;
    }

}
