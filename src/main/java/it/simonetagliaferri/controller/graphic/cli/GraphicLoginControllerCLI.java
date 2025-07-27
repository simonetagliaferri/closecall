package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.infrastructure.AppContext;
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
        this.controller = new LoginLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getLoginDAO(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO());
    }

    public void start() {
        LoginCLIView.LoginCommand choice;
        boolean loginScreen = true;
        while (loginScreen) {
            choice = view.showMenu();
            switch (choice) {
                case LOGIN:
                    loginScreen=login();
                    break;
                case SIGNUP:
                    signup();
                    break;
                case QUIT: // Exit case.
                    loginScreen=false;
                    break;
                default:
            }
        }
    }

    public boolean login() {
        boolean result;
        UserBean user = new UserBean(view.getUsername(), view.getPassword());
        result = this.controller.login(user);
        if (result) {
            navigationManager.goToDashboard(this.controller.getCurrentUserRole());
            return false;
        }
        else {
            view.failedLogin();
            return true;
        }
    }

    // Signup done in steps so that if an already existing username or email are chosen, it fails before asking the other fields.
    public void signup() {
        boolean result;
        UserBean user = new UserBean();
        user.setUsername(getUsername());
        user.setEmail(getEmail());
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
        if (confirm()) {
            result = this.controller.signup(user);
            if (result) {
                view.successfulSignup();
            } else {
                view.failedSignup();
            }
        }
    }

    public String getUsername() {
        UserBean user = new UserBean();
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
        return user.getUsername();
    }

    public String getEmail() {
        UserBean user = new UserBean();
        boolean validEmail = false;
        while (!validEmail) {
            validEmail = true;
            while (true) {
                String email = view.getEmail();
                if (user.setEmail(email)) {
                    break;
                }
                view.invalidEmail();
            }
            if (this.controller.emailLookUp(user)) {
                validEmail = false;
                view.emailAlreadyTaken();
            }
        }
        return user.getEmail();
    }

    public boolean confirm() {
        while (true) {
            String choice = view.getConfirmation();
            if (choice.equalsIgnoreCase("Y")) {
                return true;
            }
            else if (choice.equalsIgnoreCase("N")) {
                return false;
            }
            else {
                view.invalidChoice();
            }
        }
    }
}
