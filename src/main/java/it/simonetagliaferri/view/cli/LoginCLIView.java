package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;

public class LoginCLIView {
    public int showMenu() throws IOException {
        CliUtils.println("Welcome to login");
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
    public UserBean authenticate() throws IOException {
        String username = CliUtils.prompt("Enter username: ");
        String password = CliUtils.prompt("Enter password: ");
        return new UserBean(username, password);
    }
    public UserBean signup() throws IOException {
        String email = CliUtils.prompt("Enter email: ");
        String username = CliUtils.prompt("Enter username: ");
        String password;
        while (true) {
            password = CliUtils.prompt("Enter password: ");
            String confirmPassword = CliUtils.prompt("Confirm password: ");
            if (password.equals(confirmPassword)) {
                break;
            }
            CliUtils.println("Passwords do not match. Try again.");
        }
        String roleStr;
        Role role;
        while (true) {
            roleStr = CliUtils.prompt("Enter role(Player or Host): ");
            try {
                role=Role.valueOf(roleStr.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                CliUtils.println("Invalid role. Try again.");
            }
        }
        return new UserBean(username, email, password, role);
    }
    public void successfulLogin() {
        CliUtils.println("Login successful");
    }
    public void failedLogin() {
        CliUtils.println("Login failed");
    }
    public void successfulSignup() {
        CliUtils.println("Signup successful");
    }
    public void failedSignup() {
        CliUtils.println("Signup failed");
    }
}
