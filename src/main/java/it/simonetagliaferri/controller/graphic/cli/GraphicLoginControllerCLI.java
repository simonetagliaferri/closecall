package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.utils.CliUtils;
import it.simonetagliaferri.view.cli.LoginCLIView;
import it.simonetagliaferri.controller.logic.LoginLogicController;

public class GraphicLoginControllerCLI extends GraphicController {
    LoginCLIView view;
    LoginLogicController controller;

    public GraphicLoginControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new LoginCLIView();
        this.controller = new LoginLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getLoginDAO(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO());
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
        UserBean user = new UserBean(view.getUsername(), view.getPassword());
        res = this.controller.login(user);
        if (res.getResult()== LoginResult.FAIL) {
            view.failedLogin();
            return true;
        }
        else {
            navigationManager.goToDashboard(this.controller.getCurrentUserRole());
            return false;
        }
    }

    // Signup done in 3 steps so that if an already existing username is chosen, it fails before asking the other fields.
    public void signup() {
        UserBean user = new UserBean();
        LoginResponseBean res;
        boolean validUsername = false;
        while (!validUsername) {
            validUsername = true;
            if (user.setUsername(view.getUsername())) {
                if (this.controller.userLookUp(user)) {
                    validUsername = false;
                    view.userAlreadyExists();
                }
            }
            else {
                validUsername = false;
                view.invalidUsername();
            }
        }
        boolean validEmail = false;
        while (!validEmail) {
            validEmail = true;
            while (true) {
                String email = CliUtils.prompt("Enter email: ");
                if (user.setEmail(email)) {
                    break;
                }
                CliUtils.println("Invalid email. Try again.");
            }
            if (this.controller.emailLookUp(user)) {
                validEmail = false;
                view.emailAlreadyTaken();
            }
        }
        String password;
        while (true) {
            password = view.getPassword();
            String confirmPassword = view.getPasswordConfirm();
            if (user.setPassword(password, confirmPassword)) {
                break;
            }
            view.passwordsDoNotMatch();
        }
        String roleStr;
        while (true) {
            roleStr = view.getRole();
            if (user.setRole(roleStr)) {
                break;
            }
            view.invalidRole();
        }
        while (true) {
            String choice = view.getConfirmation();
            if (choice.equalsIgnoreCase("Y")) {
                break;
            }
            else if (choice.equalsIgnoreCase("N")) {
                return;
            }
            else {
                view.invalidChoice();
            }
        }
        res = this.controller.signup(user);
        if (res.getResult() == LoginResult.SUCCESS) {
            view.successfulSignup();
        } else {
            view.failedSignup();
        }
    }
}
