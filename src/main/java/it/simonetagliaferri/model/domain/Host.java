package it.simonetagliaferri.model.domain;

public class Host extends User{
    public Host(String username, String email, Role role) {
        super(username, email, role);
    }
    public Host(String username, String email) {
        super(username, email);
    }
}
