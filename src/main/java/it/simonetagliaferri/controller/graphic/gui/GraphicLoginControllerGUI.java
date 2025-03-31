package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.controller.logic.LoginController;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;


public class GraphicLoginControllerGUI extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/start.css").toExternalForm());
        Group scalable = (Group) scene.lookup("#scalableContent");
        double baseWidth = 1280;
        double baseHeight = 720;
        stage.setMinWidth(baseWidth/2);
        stage.setMinHeight(baseHeight/2);
        bindScale(scene, scalable, baseWidth, baseHeight);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("CloseCall");
        stage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }

    private void bindScale(Scene scene, Group scalable, double baseWidth, double baseHeight) {
        ChangeListener<Number> listener = (obs, oldVal, newVal) -> {
            double scaleX = scene.getWidth() / baseWidth;
            double scaleY = scene.getHeight() / baseHeight;
            double scale = Math.min(scaleX, scaleY);
            scalable.setScaleX(scale);
            scalable.setScaleY(scale);
        };

        scene.widthProperty().addListener(listener);
        scene.heightProperty().addListener(listener);
    }
    LoginController controller = new LoginController();
    private enum UIState {
        USERNAME_INPUT,
        PASSWORD_INPUT,
        SIGNUP
    }

    private UIState state = UIState.USERNAME_INPUT;

    @FXML private Group scalableContent;
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
    @FXML private Spinner roleSpinner;

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
        Platform.runLater(() -> subHyper.requestFocus());
    }

    @FXML
    private void handleMainButton() {
        if (state == UIState.USERNAME_INPUT) {
            String username = usernameField.getText().trim();
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
            switchToLogin();
        }
        else if (state == UIState.USERNAME_INPUT) {
            switchToSignup();
        }
        else if (state == UIState.SIGNUP) {
            clearSignup();
            switchToLogin();
        }
    }

    @FXML
    private void handleSignupButton() {
        //String selectedRole = roleSpinner.getValue();
        //UserBean user = new UserBean(usernameField.getText(), emailField.getText(), passwordField.getText());
        //this.controller.signup(user);
        clearSignup();
        switchToLogin();

        welcomeText.setText("Signed up successfully");
    }

    @FXML
    private void handleGoogleLogin() {
        tempMessage("This is not implemented yet.");
    }

    @FXML
    private void handlePassResetHyper() {
        tempMessage("This is not implemented yet.");
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

        mainButton.requestFocus();
        state = UIState.USERNAME_INPUT;
    }

    private boolean showingTempMessage = false;

    private void tempMessage(String message) {
        if (showingTempMessage) return;

        showingTempMessage = true;
        String old = welcomeText.getText();
        welcomeText.setText(message);

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

}
