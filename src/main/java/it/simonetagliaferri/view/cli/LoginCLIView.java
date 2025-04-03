package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.CliUtils;

public class LoginCLIView {
    public int showMenu() {
        CliUtils.println("Welcome to the court.");
        while (true) {
            CliUtils.println("1. Login");
            CliUtils.println("2. Sign up");
            CliUtils.println("3. Exit");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice >= 1 && choice <= 3) {
                return choice;
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }
    public UserBean authenticate() {
        String username = CliUtils.prompt("Enter username: ");
        String password = CliUtils.prompt("Enter password: ");
        return new UserBean(username, password);
    }

    public UserBean signupFirstStep() {
        String username = CliUtils.prompt("Enter username: ");
        return new UserBean(username);
    }

    public UserBean signupSecondStep(UserBean user) {
        while (true) {
            String email = CliUtils.prompt("Enter email: ");
            user.setEmail(email);

            /* For email, password and role input validation I decided to call the bean's methods directly from the view,
            * without going through the GraphicController because I wanted to have direct validation after
            * each field was filled by the user and not just at the end, it's just less messy doing it this way*/

            if (user.validEmail()) {
                break;
            }
            CliUtils.println("Invalid email. Try again.");
        }
        String password;
        while (true) {
            password = CliUtils.prompt("Enter password: ");
            user.setPassword(password);
            String confirmPassword = CliUtils.prompt("Confirm password: ");
            if (user.confirmPassword(confirmPassword)) {
                break;
            }
            CliUtils.println("Passwords do not match. Try again.");
        }
        String roleStr;
        while (true) {
            roleStr = CliUtils.prompt("Enter role(Player or Host): ");
            try {
                Role role = user.validRole(roleStr);
                user.setRole(role);
                break;
            } catch (IllegalArgumentException e) {
                CliUtils.println("Invalid role. Try again.");
            }
        }
        return user;
    }

    public void userAlreadyExists() {
        CliUtils.println("Username already taken.");
    }

    public void successfulLogin() {
        CliUtils.println("Login successful");
    }
    public void failedLogin() {
        CliUtils.println("Invalid credentials. Try again.");
    }
    public void successfulSignup() {
        CliUtils.println("Signup successful");
    }
    public void failedSignup() {
        CliUtils.println("Signup failed. Try again.");
    }
}
