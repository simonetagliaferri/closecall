package it.simonetagliaferri.controller.graphic.cli;

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
                new GraphicStartControllerCLI().start();
        }
    }

    public void login() {
        UserBean user;
        int res;
        try {
            user=view.authenticate();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res=this.controller.login(user);
        if (res==1) {
            view.successfulLogin();
        }
        else {
            view.failedLogin();
        }
    }

    public void signup() {
        UserBean user;
        int res;
        try {
            user=view.signup();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res=this.controller.signup(user);
        if (res==1) {
            view.successfulSignup();
        }
        else {
            view.failedSignup();
        }
    }

}
