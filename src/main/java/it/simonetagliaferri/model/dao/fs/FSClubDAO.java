package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

public class FSClubDAO extends FSDAO implements ClubDAO {

    private static final String FILE_NAME = "clubs.db";
    private final Map<String, Club> clubs;

    public FSClubDAO(Path baseDir) {
        super(baseDir, FILE_NAME);
        clubs = new HashMap<>();
        loadClubs();
    }

    @SuppressWarnings("unchecked")
    private void loadClubs() {
        Path f = file();
        try (ObjectInputStream ois = new ObjectInputStream(Files.newInputStream(f))) {
            Object obj = ois.readObject();
            if (obj instanceof Map) {
                Map<String, Club> loaded = (Map<String, Club>) obj;
                clubs.clear();
                clubs.putAll(loaded);
            }
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error loading clubs", e);
        }
    }

    private void saveClubs() {
        Path f = file();
        try (ObjectOutputStream oos = new ObjectOutputStream(Files.newOutputStream(f, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
            oos.writeObject(clubs);
        } catch (IOException e) {
            throw new DAOException("Error saving clubs", e);
        }
    }

    @Override
    public Club getClubByHostName(String hostName) {
        return clubs.get(hostName);
    }

    @Override
    public void saveClub(Club club) {
        clubs.put(club.getOwnerUsername(), club);
        saveClubs();
    }

}
