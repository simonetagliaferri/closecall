package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.model.domain.Role;
import it.simonetagliaferri.utils.CliUtils;

import java.io.IOException;

public class LoginCLIView {
    public int showMenu() throws IOException {
        System.out.println("Welcome to login");
        while (true) {
            System.out.println("1. Login");
            System.out.println("2. Sign up");
            System.out.println("3. Go back");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice >= 1 && choice <= 3) {
                return choice;
            }
            System.out.println("Invalid choice. Try again.");
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
            System.out.println("Passwords do not match. Try again.");
        }
        String roleStr;
        Role role;
        while (true) {
            System.out.print("Enter role(Player or Host): ");
            roleStr = CliUtils.prompt("Enter role: ");
            try {
                role=Role.valueOf(roleStr.toUpperCase());
                break;
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid role. Try again.");
            }
        }
        return new UserBean(username, email, password, role);
    }
    public void successfulLogin() {
        System.out.println("Login successful");
    }
    public void failedLogin() {
        System.out.println("Login failed");
    }
    public void successfulSignup() {
        System.out.println("Signup successful");
    }
    public void failedSignup() {
        System.out.println("Signup failed");
    }
}
