package it.simonetagliaferri.model.dao.fs;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import it.simonetagliaferri.model.dao.ClubDAO;
import it.simonetagliaferri.model.domain.Club;
import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FSClubDAO extends FSDAO implements ClubDAO {

    private final Map<String, List<Club>> clubs;

    protected FSClubDAO() {
        super("clubs.json");
        clubs = new HashMap<>();
        loadFromFile();
    }

    @Override
    protected void loadFromFile() {
        if (!file.exists()) return;

        try {
            // Deserialize the map of hosts from JSON
            TypeFactory typeFactory = mapper.getTypeFactory();

            JavaType listType = typeFactory.constructCollectionType(List.class, Club.class);
            JavaType mapType = typeFactory.constructMapLikeType(HashMap.class, typeFactory.constructType(String.class), listType);

            Map<String, List<Club>> loaded = mapper.readValue(file, mapType);

            if (loaded != null) {
                clubs.clear();
                clubs.putAll(loaded);
            }

            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error loading hosts: " + e.getMessage());
        }
    }

    @Override
    public List<Club> getClubs(String hostName) {
        reloadIfChanged();
        return clubs.get(hostName);
    }

    @Override
    public void saveClub(Club club) {
        reloadIfChanged();
        clubs.computeIfAbsent(club.getOwner().getUsername(), k -> new ArrayList<>()).add(club);
        saveClubs();
    }

    private void saveClubs() {
        try {
            // Serialize the map of hosts back to JSON
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, clubs);
            updateLastModified();
        } catch (IOException e) {
            CliUtils.println("Error saving hosts: " + e.getMessage());
        }
    }

    public List<Club> getClubsByCity(String city) {
        List<Club> result = new ArrayList<>();
        for (List<Club> clubs : this.clubs.values()) {
            for (Club club : clubs) {
                if (club.getCity().equals(city)) {
                    result.add(club);
                }
            }
        }
        return result;
    }

    @Override
    public Club getClubByName(String hostName, String clubName) {
        //TODO
        return null;
    }

    @Override
    public boolean clubAlreadyExists(Club club) {
        //TODO
        return false;
    }

}
