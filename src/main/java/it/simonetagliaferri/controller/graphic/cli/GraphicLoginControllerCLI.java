package it.simonetagliaferri.controller.graphic.cli;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.LoginApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.view.cli.LoginCLIView;

public class GraphicLoginControllerCLI extends GraphicController {

    LoginCLIView view;
    LoginApplicationController controller;

    public GraphicLoginControllerCLI(AppContext appContext) {
        super(appContext);
        this.view = new LoginCLIView();
        this.controller = new LoginApplicationController(appContext.getSessionManager(),
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
                    loginScreen = login();
                    break;
                case SIGNUP:
                    signup();
                    break;
                case QUIT:
                    loginScreen = false;
                    break;
            }
        }
    }

    public boolean login() {
        UserBean user = new UserBean(view.getUsername(), view.getPassword());
        if (this.controller.login(user)) {
            Role role = this.controller.getCurrentUserRole();
            navigationManager.goToDashboard(role);
            return false;
        } else {
            view.failedLogin();
            return true;
        }
    }

    // Signup done in steps so that if an already existing username or email are chosen, it fails before asking the other fields.
    public void signup() {
        UserBean user = new UserBean();
        user.setUsername(getUsername());
        user.setEmail(getEmail());
        String password;
        while (true) {
            password = view.getPassword();
            String confirmPassword = view.getPasswordConfirm();
            /*
             * Check done in bean since it's not really a business rule that pass and confPass should match.
             * It's more of a syntax problem to ensure the user doesn't mistype the password.
             */
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
            this.controller.signup(user);
            view.successfulSignup();
        }
    }

    public String getUsername() {
        UserBean user = new UserBean();
        boolean validUsername = false;
        while (!validUsername) {
            validUsername = true;
            if (user.setUsername(view.getUsername())) {
                if (!this.controller.isUsernameValid(user)) {
                    validUsername = false;
                    view.emailAsUsername();
                } else if (this.controller.usernameLookUp(user)) {
                    validUsername = false;
                    view.userAlreadyExists();
                }
            } else {
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
            } else if (choice.equalsIgnoreCase("N")) {
                return false;
            } else {
                view.invalidChoice();
            }
        }
    }
}
