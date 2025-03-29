package it.simonetagliaferri.beans;

import it.simonetagliaferri.model.domain.Role;

public class LoginResponseBean {
    private LoginResult result;
    private String username;
    private Role role; // You could use String if you prefer, but Role is cleaner

    public LoginResponseBean(LoginResult result, Role role) {
        this.result = result;
        this.role = role;
    }
    public LoginResponseBean(LoginResult result) {
        this.result = result;
    }

    public LoginResult getResult() {
        return result;
    }
}
