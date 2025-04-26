package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.beans.PlayerBean;

public class Player extends User{
    public Player(String username, String email, Role role) {
        super(username, email, role);
    }
    public Player(String username, String email) {
        super(username, email);
    }

    public Player(PlayerBean playerBean) {
        super(playerBean);
    }

    public static Player fromUser(User user) {
        return new Player(user.getUsername(), user.getEmail(), user.getRole());
    }
}
