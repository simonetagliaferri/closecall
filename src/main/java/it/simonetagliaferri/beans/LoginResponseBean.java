package it.simonetagliaferri.beans;


public class LoginResponseBean {
    private final LoginResult result;

    public LoginResponseBean(LoginResult result) {
        this.result = result;
    }

    public LoginResult getResult() {
        return result;
    }
}
