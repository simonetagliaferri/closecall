package it.simonetagliaferri.controller.graphic;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class GraphicLoginControllerGUI extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("CloseCall");
        stage.setMinWidth(600);
        stage.setMinHeight(400);
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    private enum UIState {
        USERNAME_INPUT,
        PASSWORD_INPUT
    }

    private UIState state = UIState.USERNAME_INPUT;

    @FXML private Text welcomeText;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Button mainButton;
    @FXML private Button googleLogin;
    @FXML private TextFlow subtitle;
    @FXML private Text subText;
    @FXML private Hyperlink subHyper;
    @FXML private HBox divider;
    @FXML private Hyperlink passResetHyper;
    @FXML private TextField emailField;
    @FXML private TextField passwordField1;
    @FXML private TextField confirmPassField;
    @FXML private Button signupButton;

    @FXML
    private void initialize() {
        state = UIState.USERNAME_INPUT;
        usernameField.setVisible(true);
        passwordField.setVisible(false);
        passResetHyper.setVisible(false);
        Platform.runLater(() -> mainButton.requestFocus());
    }

    @FXML
    private void handleMainButton() {
        if (state == UIState.USERNAME_INPUT) {
            String username = usernameField.getText().trim();
            if (username.isBlank()) {
                // Optional: show warning to user
                return;
            }

            // Transition to password phase
            welcomeText.setText("Welcome back!");
            usernameField.setVisible(false);
            passwordField.setVisible(true);
            mainButton.setText("Sign in");

            subText.setText(username);
            subHyper.setText("switch account");
            passResetHyper.setVisible(true);

            googleLogin.setVisible(false);
            divider.setVisible(false);
            mainButton.requestFocus();

            state = UIState.PASSWORD_INPUT;
        }

        else if (state == UIState.PASSWORD_INPUT) {
            String password = passwordField.getText().trim();
            if (password.isBlank()) {
                // Optional: show warning
                return;
            }

            System.out.println("Login: " + usernameField.getText() + " / " + password);
            // TODO: Call logic controller for login
        }
    }

    @FXML
    private void handleSubHyperClick() {
        // Reset UI to username entry state
        if (state == UIState.PASSWORD_INPUT) {
            usernameField.clear();
            passwordField.clear();

            usernameField.setVisible(true);
            passwordField.setVisible(false);
            passResetHyper.setVisible(false);

            welcomeText.setText("Welcome to the court.");
            mainButton.setText("Continue");

            subText.setText("First time here?");
            subHyper.setText("Create account");

            googleLogin.setVisible(true);
            divider.setVisible(true);
            subtitle.setVisible(true);

            mainButton.requestFocus();
            state = UIState.USERNAME_INPUT;
        }
        else if (state == UIState.USERNAME_INPUT) {
            switchToSignup();
        }
    }

    @FXML
    private void handleSignupButton() {
        welcomeText.setText("Signed up successfully!");
        mainButton.setVisible(true);
        googleLogin.setVisible(true);
        divider.setVisible(true);
        subtitle.setVisible(true);
        emailField.setVisible(false);
        passwordField1.setVisible(false);
        confirmPassField.setVisible(false);
        signupButton.setVisible(false);
        passwordField.clear();

        usernameField.setVisible(true);
        passwordField.setVisible(false);
        passResetHyper.setVisible(false);

        mainButton.setText("Continue");

        subText.setText("First time here?");
        subHyper.setText("Create account");


        mainButton.requestFocus();
        state = UIState.USERNAME_INPUT;
    }

    private void switchToSignup() {
        mainButton.setVisible(false);
        googleLogin.setVisible(false);
        divider.setVisible(false);
        subtitle.setVisible(false);
        emailField.setVisible(true);
        passwordField1.setVisible(true);
        confirmPassField.setVisible(true);
        signupButton.setVisible(true);
    }

}
