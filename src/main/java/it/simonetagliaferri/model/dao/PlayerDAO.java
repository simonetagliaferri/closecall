package it.simonetagliaferri.model.dao;

import it.simonetagliaferri.model.domain.Player;

public interface PlayerDAO {
    Player findByUsername(String username);
    Player findByEmail(String email);
    void addPlayer(Player player);
    void updatePlayer(Player player);
}
