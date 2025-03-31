package it.simonetagliaferri.beans;

import it.simonetagliaferri.model.domain.Role;
import org.apache.commons.validator.routines.EmailValidator;

public class UserBean {
    private String username;
    private String password;
    private String email;
    private Role role;
    public UserBean(String username, String password) {
        this.username = username;
        this.password = password;
    }
    public UserBean(String username, String email, String password, Role role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public boolean validEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public boolean confirmPassword(UserBean user, String password) {
        return user.getPassword().equals(password);
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
