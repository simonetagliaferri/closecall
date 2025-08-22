package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.SendPlayerInviteApplicationController;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.DateTimeException;
import java.time.LocalDate;

public class GraphicInvitePlayersHostControllerGUI extends GraphicController implements GUIController {

    @FXML
    RadioButton emailPlayer1;
    private TournamentBean tournamentBean;
    private SendPlayerInviteApplicationController controller;
    @FXML
    private Button confirmButton;
    @FXML
    private TextField player1NameField;
    @FXML
    private TextField player1Message;
    @FXML
    private DatePicker expireDatePicker;
    @FXML
    private Label expDateLabel;
    @FXML
    private VBox inviteDetails;
    @FXML
    private VBox invitedTeams;
    @FXML
    private Label title;
    private TextField player2NameField;
    private TextField player2Message;
    private RadioButton emailPlayer2;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        postInit(appContext);
    }

    public void setTournamentBean(TournamentBean tournamentBean) {
        this.tournamentBean = tournamentBean;
    }

    public void postInit(AppContext appContext) {
        BooleanBinding fieldEmpty = player1NameField.textProperty().isEmpty();
        confirmButton.disableProperty().bind(fieldEmpty);
        Platform.runLater(() -> {
            this.controller = new SendPlayerInviteApplicationController(appContext.getSessionManager(),
                    appContext.getDAOFactory().getPlayerDAO(), appContext.getDAOFactory().getTournamentDAO(),
                    appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO(),
                    tournamentBean);
            expireDatePicker.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(controller.maxExpireDate()) || date.isBefore(controller.minExpireDate()));
                }
            });
            expireDatePicker.setValue(this.controller.maxExpireDate());
            if (!tournamentBean.isSingles()) {
                ObservableList<Node> children = inviteDetails.getChildren();
                VBox vbox = new VBox();
                player2NameField = new TextField();
                player2NameField.setPromptText("Enter player's username/email address");
                player2Message = new TextField();
                player2Message.setPromptText("Add a message to the invite");
                emailPlayer2 = new RadioButton();
                emailPlayer2.setText("Send email");
                player2Message.setDisable(true);
                emailPlayer2.setDisable(true);
                player2NameField.textProperty().addListener((observable, oldValue, newValue) -> enablePlayer2Message());
                vbox.getChildren().add(player2NameField);
                vbox.getChildren().add(player2Message);
                vbox.getChildren().add(emailPlayer2);
                children.add(children.size() - 1, vbox);
            }
        });
    }

    public void enablePlayer2Message() {
        if (!player2NameField.getText().trim().isEmpty()) {
            player2Message.setDisable(false);
            emailPlayer2.setDisable(false);
        } else {
            player2Message.clear();
            player2Message.setDisable(true);
            emailPlayer2.setDisable(true);
        }
    }

    public void newInvite() {
        if (tournamentBean.isSingles() && this.controller.noSingleSpotsAvailable()) {
            title.setText("There are no more open spots available.");
            confirmButton.disableProperty();
        } else if (!tournamentBean.isSingles() && this.controller.noTeamSpotsAvailable()) {
            title.setText("There are no more spots to invite another team.");
            confirmButton.disableProperty();
        } else {
            title.setText("Invite players to the tournament");
        }
    }

    public String getMessagePlayer(TextField playerMessage) {
        if (playerMessage.getText().trim().isEmpty()) {
            return null;
        }
        return playerMessage.getText().trim();
    }

    public boolean sendEmail(RadioButton email) {
        return email.isSelected();
    }

    public void inviteTeam() {
        if (tournamentBean.isSingles()) {
            invitePlayer();
        } else {
            invite();
        }
    }

    public void invitePlayer() {
        PlayerBean playerBean = getPlayerBean(player1NameField);
        if (playerBean != null && playerRegisteredOrNotInvited(playerBean, sendEmail(emailPlayer1), player1NameField)) {
            title.setText("Invite players to the tournament");
            InviteBean inviteBean = new InviteBean(playerBean, expireDatePicker.getValue(), getMessagePlayer(player1Message), sendEmail(emailPlayer1));
            this.controller.invitePlayer(inviteBean);
            addInvitedTeam(playerBean.getUsername());
            newInvite();
        }
    }

    private PlayerBean getPlayerBean(TextField playerField) {
        String playerName = playerField.getText();
        PlayerBean playerBean;
        if (playerName.isEmpty()) {
            return null;
        } else {
            playerBean = this.controller.isPlayerValid(playerName);
            if (playerBean == null) {
                if (this.controller.isEmail(playerName)) {
                    title.setText("An invite email will be sent to the entered email address: " + playerName);
                    playerBean = new PlayerBean(playerName, playerName);
                } else {
                    title.setText("The player " + playerName + " is not registered. Use an email address to send the invite via email.");
                    return null;
                }
            }
        }
        return playerBean;
    }

    public void addInvitedTeam(String... players) {
        boolean first = true;
        for (String s : players) {
            Label player;
            if (first) {
                int number = invitedTeams.getChildren().size();
                player = new Label(number + ". " + s);
                first = false;
            } else {
                player = new Label("    " + s);
            }
            invitedTeams.getChildren().add(player);
        }
    }

    private boolean playerRegisteredOrNotInvited(PlayerBean playerBean, boolean sendEmail, TextField playerNameField) {
        if (this.controller.playerAlreadyInvited(playerBean)) {
            title.setText("Player " + playerNameField.getText() + " has already been invited.");
            playerNameField.clear();
            return false;
        } else if (!this.controller.isPlayerRegistered(playerBean) && !sendEmail) {
            title.setText("The player " + playerNameField.getText() + " is not registered. You have to enable the email invite.");
            playerNameField.clear();
            return false;
        }
        return true;
    }

    public void invite() {
        PlayerBean playerBean1 = getPlayerBean(player1NameField);
        PlayerBean playerBean2 = getPlayerBean(player2NameField);
        InviteBean inviteBean1;
        InviteBean inviteBean2;
        if (playerBean1 != null && playerRegisteredOrNotInvited(playerBean1, sendEmail(emailPlayer1), player1NameField)) {
            inviteBean1 = new InviteBean(playerBean1, expireDatePicker.getValue(), getMessagePlayer(player1Message), sendEmail(emailPlayer1));
            if (playerBean2 != null && playerRegisteredOrNotInvited(playerBean2, sendEmail(emailPlayer2), player2NameField)) {
                inviteBean2 = new InviteBean(playerBean2, expireDatePicker.getValue(), getMessagePlayer(player2Message), sendEmail(emailPlayer2));
                this.controller.inviteTeam(inviteBean1, inviteBean2);
                addInvitedTeam(playerBean1.getUsername(), playerBean2.getUsername());
                newInvite();
            } else if (playerBean2 == null && player2NameField.getText().isEmpty()) {
                this.controller.invitePlayer(inviteBean1);
                addInvitedTeam(playerBean1.getUsername());
                newInvite();
            }
        }
    }

    public void checkExpireDate() {
        try {
            expDateLabel.setText("Invite expire date");
            expDateLabel.setTextFill(Color.BLACK);
            expireDatePicker.getValue();
        } catch (DateTimeException | InvalidDateException e) {
            expDateLabel.setText("The chosen start date is not a valid date.");
            expDateLabel.setTextFill(Color.RED);
        }
    }

    @FXML
    private void goHome() {
        navigationManager.goToDashboard(Role.HOST);
    }
}
