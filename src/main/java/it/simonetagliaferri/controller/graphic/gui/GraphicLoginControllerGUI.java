package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.beans.LoginResponseBean;
import it.simonetagliaferri.beans.LoginResult;
import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.LoginLogicController;
import it.simonetagliaferri.model.domain.Role;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;


public class GraphicLoginControllerGUI extends GraphicController {

    private LoginLogicController controller;
    private UIState state = UIState.USERNAME_INPUT;
    @FXML
    private Text welcomeText;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button mainButton;
    @FXML
    private Button googleLogin;
    @FXML
    private TextFlow subtitle;
    @FXML
    private Text subText;
    @FXML
    private Hyperlink subHyper;
    @FXML
    private HBox divider;
    @FXML
    private Hyperlink passResetHyper;
    @FXML
    private TextField emailField;
    @FXML
    private TextField passwordField1;
    @FXML
    private TextField confirmPassField;
    @FXML
    private Button signupButton;
    @FXML
    private Spinner roleSpinner;
    private boolean showingTempMessage = false;

    @Override
    public void setAppContext(AppContext appContext) {
        this.appContext = appContext;
        this.controller = new LoginLogicController(appContext);
    }


    @FXML
    private void initialize() {
            state = UIState.USERNAME_INPUT;
            roleSpinner.setValueFactory(new SpinnerValueFactory.ListSpinnerValueFactory<>(
                    FXCollections.observableArrayList("Host", "Player")
            ));
            roleSpinner.getValueFactory().setWrapAround(true);
            roleSpinner.setVisible(false);
            usernameField.setVisible(true);
            passwordField.setVisible(false);
            passResetHyper.setVisible(false);
            bindMainButtonToFieldStates();
            bindSignupButtonToFieldStates();
            Platform.runLater( () -> subHyper.requestFocus());
    }

    @FXML
    private void handleMainButton() {
        if (state == UIState.USERNAME_INPUT) {
            String username = usernameField.getText().trim();
            if (this.controller.userLookUp(new UserBean(username))) {
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
            else {
                welcomeText.setText("Username not found.");
                usernameField.clear();
            }
        } else if (state == UIState.PASSWORD_INPUT) {
            String password = passwordField.getText().trim();
            if (login(usernameField.getText(), password)==LoginResult.FAIL) {
                welcomeText.setText("Log in failed");
            }
            else {
                appContext.getNavigationManager().goToDashboard(this.controller.getCurrentUserRole());
            }
        }
    }

    @FXML
    private void handleSubHyperClick() {
        // Reset UI to username entry state
        if (state == UIState.PASSWORD_INPUT) {
            switchToLogin();
        } else if (state == UIState.USERNAME_INPUT) {
            switchToSignup();
        } else if (state == UIState.SIGNUP) {
            clearSignup();
            switchToLogin();
        }
    }

    @FXML
    private void handleSignupButton() {
        UserBean user = new UserBean(usernameField.getText(), emailField.getText(),
                passwordField1.getText(), Role.valueOf(roleSpinner.getValue().toString().toUpperCase()));
        LoginResponseBean response;
        if (this.controller.userLookUp(user)) {
            usernameField.clear();
            roleSpinner.requestFocus();
            usernameField.setPromptText("Username already exists");
        } else if (!user.confirmPassword(confirmPassField.getText())) {
            confirmPassField.clear();
            roleSpinner.requestFocus();
            passwordField.setPromptText("Passwords do not match");
            confirmPassField.setPromptText("Passwords do not match");
        } else if (!user.validEmail()) {
            emailField.clear();
            roleSpinner.requestFocus();
            emailField.setPromptText("Invalid email");
        } else if (this.controller.emailLookUp(user)) {
            emailField.clear();
            roleSpinner.requestFocus();
            emailField.setPromptText("Email already in use");
        }
        else {
            response = this.controller.signup(user);
            if (response.getResult()==LoginResult.SUCCESS) {
                clearSignup();
                switchToLogin();
                welcomeText.setText("Signed up successfully");
            }
            else {
                welcomeText.setText("Signup failed");
            }
        }
    }

    @FXML
    private void handleGoogleLogin() {
        tempMessage();
    }

    @FXML
    private void handlePassResetHyper() {
        tempMessage();
    }

    private void switchToSignup() {
        subText.setText("Made a mistake?");
        subHyper.setText("Go back to login");
        mainButton.setVisible(false);
        googleLogin.setVisible(false);
        divider.setVisible(false);
        emailField.setVisible(true);
        passwordField1.setVisible(true);
        confirmPassField.setVisible(true);
        signupButton.setVisible(true);
        signupButton.setDefaultButton(true);
        roleSpinner.setVisible(true);
        state = UIState.SIGNUP;
    }

    private void clearSignup() {
        emailField.clear();
        passwordField1.clear();
        confirmPassField.clear();
        signupButton.setDefaultButton(false);
        emailField.setVisible(false);
        passwordField1.setVisible(false);
        confirmPassField.setVisible(false);
        signupButton.setVisible(false);
        roleSpinner.setVisible(false);
    }

    private void switchToLogin() {
        usernameField.clear();
        passwordField.clear();
        mainButton.requestFocus();
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
        mainButton.setVisible(true);

        state = UIState.USERNAME_INPUT;
    }

    private void tempMessage() {
        if (showingTempMessage) return;

        showingTempMessage = true;
        String old = welcomeText.getText();
        welcomeText.setText("Not implemented yet.");

        PauseTransition pause = new PauseTransition(Duration.millis(500));
        pause.setOnFinished(e -> {
            welcomeText.setText(old);
            showingTempMessage = false;
        });
        pause.play();
    }

    private void bindMainButtonToFieldStates() {
        mainButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        (usernameField.isVisible() && usernameField.getText().trim().isEmpty()) ||
                                (passwordField.isVisible() && passwordField.getText().trim().isEmpty()),
                usernameField.textProperty(),
                passwordField.textProperty(),
                usernameField.visibleProperty(),
                passwordField.visibleProperty()
        ));
    }

    private void bindSignupButtonToFieldStates() {
        signupButton.disableProperty().bind(Bindings.createBooleanBinding(() ->
                        (emailField.isVisible() && emailField.getText().trim().isEmpty()) ||
                                (passwordField1.isVisible() && passwordField1.getText().trim().isEmpty()) ||
                                (confirmPassField.isVisible() && confirmPassField.getText().trim().isEmpty()),
                emailField.textProperty(),
                passwordField1.textProperty(),
                confirmPassField.textProperty(),
                emailField.visibleProperty(),
                passwordField1.visibleProperty(),
                confirmPassField.visibleProperty()
        ));
    }

    private LoginResult login(String username, String password) {
        LoginResponseBean loginResponse = this.controller.login(new UserBean(username, password));
        return loginResponse.getResult();
    }

    private enum UIState {
        USERNAME_INPUT,
        PASSWORD_INPUT,
        SIGNUP
    }

}
