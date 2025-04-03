package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.view.cli.LoginCLIView;
import it.simonetagliaferri.controller.logic.LoginController;

import java.io.IOException;

public class GraphicLoginControllerCLI {
    LoginCLIView view = new LoginCLIView();
    LoginController controller = new LoginController();
    public void start() {
        int choice;
        try {
            choice = view.showMenu();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        switch (choice) {
            case 1:
                login();
                break;
            case 2:
                signup();
                break;
            case 3:
                this.controller.end();
                break;
            default:
        }
    }

    public void login() {
        UserBean user;
        LoginResponseBean res;
        try {
            user=view.authenticate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res=this.controller.login(user);
        switch (res.getResult()) {
            case SUCCESS:
                view.successfulLogin();
                break;
            case FAIL:
                view.failedLogin();
        }
    }

    public void signup() {
        UserBean user=null;
        LoginResponseBean res;
        try {
            boolean validUsername=false;
            while (!validUsername) {
                validUsername=true;
                user = view.signupFirstStep();
                if (this.controller.userLookUp(user)) {
                    validUsername=false;
                    view.userAlreadyExists();
                }
            }
            user=view.signupSecondStep(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res=this.controller.signup(user);
        switch (res.getResult()) {
            case SUCCESS:
                view.successfulSignup();
                break;
            case FAIL:
                view.failedSignup();
                break;
        }
        start();
    }

}
