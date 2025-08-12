package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.exception.DAOException;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;

import java.io.*;
import java.util.*;

public class FSClubDAO implements ClubDAO {

    private static final String FILE = "clubs.db";
    Map<String, Club> clubs;

    public FSClubDAO() {
        clubs = new HashMap<>();
        loadClubs();
    }

    @SuppressWarnings("unchecked")
    private void loadClubs() {
        File file = new File(FILE);
        if (!file.exists()) return; // Start empty if no file
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            clubs = (Map<String, Club>) ois.readObject();
        } catch (EOFException e) {
            // Empty file, ignore
        } catch (IOException | ClassNotFoundException e) {
            throw new DAOException("Error loading clubs", e);
        }
    }

    private void saveClubs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE))) {
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
        clubs.put(club.getOwner().getUsername(), club);
        saveClubs();
    }

}
