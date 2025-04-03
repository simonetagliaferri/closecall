package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.view.cli.LoginCLIView;
import it.simonetagliaferri.controller.logic.LoginController;

public class GraphicLoginControllerCLI {
    LoginCLIView view = new LoginCLIView();
    LoginController controller = new LoginController();
    public void start() {
        int choice = view.showMenu();
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
        LoginResponseBean res;
        UserBean user=view.authenticate();
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
