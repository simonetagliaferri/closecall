package it.simonetagliaferri.model.domain;

import it.simonetagliaferri.utils.PasswordUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {

    private final String username;
    private final String email;
    private String password;
    private Role role;

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

    public User(String username) {
        this.username = username;
        this.email = null;
        this.password = null;
        this.role = null;
    }

    public boolean isUsernameValid() {
        return !EmailValidator.getInstance().isValid(this.username); // The username can't be an email address.
    }

    private String hashPassword(String password) {
        return PasswordUtils.sha256Hex(password);
    }

    public void hashPassword() {
        this.password = hashPassword(this.password);
    }

    public boolean isPasswordCorrect(String password) {
        String hashedPassword = hashPassword(password);
        if (this.password == null) return false;
        return this.password.equals(hashedPassword);
    }

    public boolean isHost() {
        return this.role == Role.HOST;
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

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (user == this) return true;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername());
    }
}
