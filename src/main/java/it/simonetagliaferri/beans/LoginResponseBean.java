package it.simonetagliaferri.beans;

import it.simonetagliaferri.model.domain.Role;

public class LoginResponseBean {
    private final LoginResult result;
    private String username;

    public LoginResponseBean(LoginResult result, Role role) {
        this.result = result;
        // You could use String if you prefer, but Role is cleaner
    }
    public LoginResponseBean(LoginResult result) {
        this.result = result;
    }

    public LoginResult getResult() {
        return result;
    }
}
