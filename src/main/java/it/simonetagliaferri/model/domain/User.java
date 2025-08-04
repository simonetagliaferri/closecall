package it.simonetagliaferri.model.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.simonetagliaferri.utils.PasswordUtils;
import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String email;
    private Role role;

    public User() {

    }

    public User(String username, String email, String password, Role role) {
        this.username = username;
        this.password = hashPassword(password);
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

    public void updateCredentials(String username, String password, String email) {
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
    }

    @JsonIgnore
    public boolean isUsernameValid() {
        return !EmailValidator.getInstance().isValid(this.username); // The username can't be a email address.
    }

    private String hashPassword(String password) {
        return PasswordUtils.sha256Hex(password);
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isPasswordCorrect(String password) {
        String hashedPassword = PasswordUtils.sha256Hex(password);
        return this.password.equals(hashedPassword);
    }

    @JsonIgnore
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

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof User)) return false;
        User user = (User) o;
        if (this == user) return true;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUsername());
    }
}
