package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class LoginCLIView {

    public LoginCommand showMenu() {
        CliUtils.println("Welcome to the court.");
        while (true) {
            CliUtils.println("1. Login");
            CliUtils.println("2. Sign up");
            CliUtils.println("3. Quit");
            int choice = CliUtils.promptPositiveInt("Enter your choice: ");
            if (choice >= 1 && choice <= 3) {
                return LoginCommand.values()[choice - 1];
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }

    public void emailAsUsername() {
        CliUtils.println("You can't use an email address as a username.");
    }

    public String getUsername() {
        return CliUtils.prompt("Enter username: ");
    }

    public String getPassword() {
        return CliUtils.prompt("Enter password: ");
    }

    public String getEmail() {
        return CliUtils.prompt("Enter email: ");
    }

    public String getPasswordConfirm() {
        return CliUtils.prompt("Confirm password: ");
    }

    public String getRole() {
        return CliUtils.prompt("Enter role(Player or Host): ");
    }

    public String getConfirmation() {
        return CliUtils.prompt("Are you sure you want to signup? (Y/N): ");
    }

    public void invalidRole() {
        CliUtils.println("Invalid role. Try again.");
    }

    public void invalidEmail() {
        CliUtils.println("Invalid email. Try again.");
    }

    public void passwordsDoNotMatch() {
        CliUtils.println("Passwords do not match or the password is invalid. Try again.");
    }

    public void invalidChoice() {
        CliUtils.println("Invalid choice. Try again.");
    }

    public void invalidUsername() {
        CliUtils.println("Invalid username. Try again.");
    }

    public void userAlreadyExists() {
        CliUtils.println("Username already taken.");
    }

    public void failedLogin() {
        CliUtils.println("Invalid credentials. Try again.");
    }

    public void successfulSignup() {
        CliUtils.println("Signup successful");
    }

    public void emailAlreadyTaken() {
        CliUtils.println("Email already taken. Try again.");
    }

    public enum LoginCommand {
        LOGIN,
        SIGNUP,
        QUIT
    }
}
