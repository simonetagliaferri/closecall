package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.view.cli.LoginCLIView;
import it.simonetagliaferri.controller.logic.LoginLogicController;

public class GraphicLoginControllerCLI extends GraphicController {
    LoginCLIView view;
    LoginLogicController controller;

    public GraphicLoginControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new LoginCLIView();
        this.controller = new LoginLogicController(this.appContext);
    }

    public void start() {
        int choice;
        boolean loginScreen = true;
        while (loginScreen) {
            choice = view.showMenu();
            switch (choice) {
                case 1:
                    loginScreen=login();
                    break;
                case 2:
                    signup();
                    break;
                case 3: // Exit case.
                    loginScreen=false;
                    break;
                default:
            }
        }
    }

    public boolean login() {
        LoginResponseBean res;
        UserBean user = view.authenticate();
        res = this.controller.login(user);
        if (res.getResult()== LoginResult.FAIL) {
            view.failedLogin();
            return true;
        }
        else {
            appContext.getNavigationManager().goToDashboard(this.controller.getCurrentUserRole());
            return false;
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
        if (user!=null) {
            res = this.controller.signup(user);
            if (res.getResult() == LoginResult.SUCCESS) {
                view.successfulSignup();
            } else {
                view.failedSignup();
            }
        }
    }
}
