package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.InvitePlayerLogicController;
import it.simonetagliaferri.exception.InvalidDateException;
import it.simonetagliaferri.infrastructure.AppContext;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.time.DateTimeException;
import java.time.LocalDate;

public class GraphicInvitePlayersHostControllerGUI extends GraphicController implements GUIController {
    private TournamentBean tournamentBean;
    private InvitePlayerLogicController controller;
    private InviteBean inviteBean;

    @FXML private Button confirmButton;
    @FXML private TextField player1NameField;
    @FXML private TextField player1Message;
    @FXML private DatePicker expireDatePicker;
    @FXML private Label expDateLabel;
    @FXML RadioButton emailPlayer1;
    private LocalDate expireDate;
    @FXML private VBox inviteDetails;
    @FXML private VBox invitedTeams;
    @FXML private Label title;
    private TextField player2NameField;
    private TextField player2Message;
    private RadioButton emailPlayer2;

    @FXML
    private void initialize() {
    }

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new InvitePlayerLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getPlayerDAO(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void setTournamentBean(TournamentBean tournamentBean) {
        this.tournamentBean = tournamentBean;
    }

    public void postInit() {
        Platform.runLater(() -> {
            expireDatePicker.setDayCellFactory(param -> new DateCell() {
                @Override
                public void updateItem(LocalDate date, boolean empty) {
                    super.updateItem(date, empty);
                    setDisable(empty || date.isAfter(controller.maxExpireDate(tournamentBean)) || date.isBefore(controller.minExpireDate()));
                }
            });
            expireDatePicker.setValue(this.controller.maxExpireDate(this.tournamentBean));
            if (!tournamentBean.isSingles()) {
                ObservableList<Node> children = inviteDetails.getChildren();
                VBox vbox = new VBox();
                player2NameField = new TextField();
                player2Message = new TextField();
                emailPlayer2 = new RadioButton();
                player2Message.setDisable(true);
                emailPlayer2.setDisable(true);
                player2NameField.textProperty().addListener((observable, oldValue, newValue) -> {enablePlayer2();});
                vbox.getChildren().add(player2NameField);
                vbox.getChildren().add(player2Message);
                vbox.getChildren().add(emailPlayer2);
                children.add(children.size() - 1, vbox);
            }
        });
    }

    public void enablePlayer2() {
        if (!player2NameField.getText().trim().isEmpty()) {
            player2Message.setDisable(false);
            emailPlayer2.setDisable(false);
        }
        else {
            player2Message.clear();
            player2Message.setDisable(true);
            emailPlayer2.setDisable(true);
        }
    }

    public void newInvite() {
        if (tournamentBean.isSingles() && !this.controller.spotAvailable(tournamentBean)) {
            //Stop
        }
        else if (!tournamentBean.isSingles() && !this.controller.teamAvailable(tournamentBean)) {
            //Stop
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
        }
        else {
            invite();
        }
    }

    public void invitePlayer() {
        String playerName = player1NameField.getText();
        if (playerName.isEmpty()) {
            player1NameField.setPromptText("You must enter a player name/email address");
        }
        else {
            PlayerBean playerBean = this.controller.isPlayerValid(playerName);
            if (playerBean == null) {
                player1NameField.clear();
                player1NameField.setPromptText("It's not a valid player name/email address");
                if (this.controller.isEmail(playerName)) {
                    title.setText("An invite email will be sent to the entered email address.");
                }
                else {
                    title.setText("Use an email address to send an invite email.");
                }
            }
            else {
                if (this.controller.playerAlreadyInvited(playerBean, this.tournamentBean)) {
                    title.setText("Player has already been invited.");
                }
                else {
                    this.controller.invitePlayer(playerBean, tournamentBean, expireDatePicker.getValue(), getMessagePlayer(player1Message),
                            sendEmail(emailPlayer1));
                    addInvitedTeam(playerName);
                }
            }
        }
    }

    public void addInvitedTeam(String ... players) {
        for (String player : players) {
            Label team = new Label(player);
            invitedTeams.getChildren().add(team);
        }
    }

    public void invite() {

    }

    public void checkExpireDate() {
        try {
            expDateLabel.setText("Invite expire date");
            expDateLabel.setTextFill(Color.BLACK);
            expireDate = expireDatePicker.getValue();
        } catch (DateTimeException | InvalidDateException e) {
            expDateLabel.setText("The chosen start date is not a valid date.");
            expDateLabel.setTextFill(Color.RED);
        }
    }
}
