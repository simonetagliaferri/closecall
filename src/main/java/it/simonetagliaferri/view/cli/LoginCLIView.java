package it.simonetagliaferri.view.cli;

import it.simonetagliaferri.utils.CliUtils;

public class LoginCLIView {

    public enum LoginCommand {
        LOGIN,
        SIGNUP,
        QUIT
    }

    public LoginCommand showMenu() {
        CliUtils.println("Welcome to the court.");
        while (true) {
            CliUtils.println("1. Login");
            CliUtils.println("2. Sign up");
            CliUtils.println("3. Quit");
            int choice = CliUtils.promptInt("Enter your choice: ");
            if (choice >= 1 && choice <= 3) {
                if (choice == 1) {
                    return LoginCommand.LOGIN;
                }
                else if (choice == 2) {
                    return LoginCommand.SIGNUP;
                }
                else {
                    return LoginCommand.QUIT;
                }
            }
            CliUtils.println("Invalid choice. Try again.");
        }
    }

    public String getUsername() {
        return CliUtils.prompt("Enter username: ");
    }

    public String getPassword() {
        return CliUtils.prompt("Enter password: ");
    }


    /* For email, password and role input validation I decided to call the bean's methods directly from the view,
   without going through the GraphicController because I wanted to have direct validation after
   each field was filled by the user and not just at the end, it's just less messy doing it this way.
   This is what is done in signupSecondStep and signupThirdStep. The need to split signup in the CLI UI is given by
   the fact that it would be inappropriate to ask the user multiple fields without giving a feedback on invalid inputs.*/

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

    public void failedSignup() {
        CliUtils.println("Signup failed. Try again.");
    }

    public void emailAlreadyTaken() {
        CliUtils.println("Email already taken. Try again.");
    }
}
