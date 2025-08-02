package it.simonetagliaferri.model.dao.jdbc;

import it.simonetagliaferri.model.dao.PlayerDAO;
import it.simonetagliaferri.model.domain.Player;

public class JDBCPlayerDAO implements PlayerDAO {
    @Override
    public Player findByUsername(String username) {
        return null;
    }

    @Override
    public Player findByEmail(String email) {
        return null;
    }

    @Override
    public void savePlayer(Player player) {

    }

}
