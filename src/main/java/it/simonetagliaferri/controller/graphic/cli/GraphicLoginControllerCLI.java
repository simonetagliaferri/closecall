package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.navigation.NavigationManager;
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
            case 3: // Exit case.
                break;
            default:
        }
    }

    public void login() {
        LoginResponseBean res;
        UserBean user = view.authenticate();
        res = this.controller.login(user);
        if (res.getResult()== LoginResult.FAIL) {
            view.failedLogin();
            start();
        }
        else {
            NavigationManager.getInstance().goToDashboard(this.controller.getCurrentUserRole());
        }
    }

    // Signup done in 2 steps so that if an already existing username is chosen, it fails before asking the other fields.
    public void signup() {
        UserBean user = null;
        LoginResponseBean res;
        boolean validUsername = false;
        while (!validUsername) {
            validUsername = true;
            user = view.signupFirstStep();
            if (this.controller.userLookUp(user)) {
                validUsername = false;
                view.userAlreadyExists();
            }
        }
        boolean validEmail = false;
        while (!validEmail) {
            validEmail = true;
            user = view.signupSecondStep(user);
            if (this.controller.emailLookUp(user)) {
                validEmail = false;
                view.emailAlreadyTaken();
            }
        }
        user = view.signupThirdStep(user);
        res = this.controller.signup(user);
        if (res.getResult()==LoginResult.SUCCESS) {
            view.successfulSignup();
        }
        else {
            view.failedSignup();
        }
        start();
    }

}
