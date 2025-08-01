package it.simonetagliaferri.beans;

import it.simonetagliaferri.model.domain.Role;
import org.apache.commons.validator.routines.EmailValidator;

public class UserBean {
    protected String username;
    private String password;
    protected String email;
    private Role role;


    public UserBean() {}
    public UserBean(String username) {
        this.username = username;
    }

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

    public UserBean(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public UserBean(String username, String email, Role role) {
        this.username = username;
        this.email = email;
        this.role = role;
    }

    private boolean validEmail(String email) {
        return EmailValidator.getInstance().isValid(email);
    }

    public String getUsername() {
        return username;
    }

    public boolean setUsername(String username) {
        if (!username.isEmpty()) {
            this.username = username;
            return true;
        }
        return false;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password, String confirmPassword) {
        if (!password.isEmpty() && password.equals(confirmPassword)) {
            this.password = password;
            return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        if (validEmail(email)) {
            this.email = email;
            return true;
        }
        else {
            this.email = null;
            return false;
        }
    }

    public Role getRole() {
        return role;
    }

    public boolean setRole(String role) {
        try {
            this.role = Role.valueOf(role.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
