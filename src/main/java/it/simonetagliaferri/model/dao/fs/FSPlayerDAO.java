package it.simonetagliaferri.model.dao.fs;

import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;

public class FSPlayerDAO implements PlayerDAO {
    @Override
    public Player findByUsername(String username) {
        return null;
    }

    @Override
    public Player findByEmail(String email) {
        return null;
    }

    @Override
    public void addPlayer(Player player) {

    }

    @Override
    public void updatePlayer(Player player) {

    }
}
