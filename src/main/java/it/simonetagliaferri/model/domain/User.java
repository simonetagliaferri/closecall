package it.simonetagliaferri.model.domain;

public class User {
    private final String username;
    private final String password;
    private final String email;
    private final Role role;

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
        this.password = null;
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
        this.role = null;
        this.password = null;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }
}
