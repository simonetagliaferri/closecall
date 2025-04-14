package it.simonetagliaferri.model.domain;

public class Player extends User{
    public Player(String username, String email, Role role) {
        super(username, email, role);
    }
    public Player(String username, String email) {
        super(username, email);
    }
}
