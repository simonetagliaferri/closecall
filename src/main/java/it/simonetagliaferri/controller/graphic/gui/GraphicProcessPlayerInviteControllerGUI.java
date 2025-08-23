package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.ProcessPlayerInviteApplicationController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.invite.InviteStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GraphicProcessPlayerInviteControllerGUI extends GraphicController implements GUIController {

    public static final String BLUE_BUTTON = "blue-button";
    List<TitledPane> invitesList;
    private ProcessPlayerInviteApplicationController controller;
    @FXML
    private Accordion tournamentList;
    @FXML
    private VBox invitesContainer;

    @Override
    public void initializeController(AppContext appContext) {
        this.controller = new ProcessPlayerInviteApplicationController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        getInvites();
    }

    private void getInvites() {
        tournamentList.getPanes().clear();
        if (invitesList != null) invitesList.clear();
        List<InviteBean> invites = controller.getInvites();
        if (invites.isEmpty()) noNewInvites();
        else {
            for (InviteBean inviteBean : invites) {
                if (invitesList == null) invitesList = new ArrayList<>();
                invitesList.clear();
                TitledPane invite = createInvitePane(inviteBean);
                tournamentList.getPanes().add(invite);
            }
        }
    }

    private void noNewInvites() {
        Label noInvites = new Label("There are no new invites for you right now.");
        invitesContainer.getChildren().clear();
        invitesContainer.getChildren().add(noInvites);
    }

    private void acceptInvite(ActionEvent event, InviteBean invite) {
        this.controller.updateInvite(invite, InviteStatus.ACCEPTED);
        askToAddClubToFavourites(event, invite);
    }

    private void declineInvite(ActionEvent event, InviteBean invite) {
        this.controller.updateInvite(invite, InviteStatus.DECLINED);
        askToAddClubToFavourites(event, invite);
    }

    private void askToAddClubToFavourites(ActionEvent event, InviteBean invite) {
        Button button = (Button) event.getSource();
        Parent parent = button.getParent();
        if (parent instanceof HBox) {
            HBox hBox = (HBox) parent;
            hBox.getChildren().clear();
            if (this.controller.isNotSubscribed(invite)) {
                VBox vBox = getParentVBox(hBox);
                if (vBox == null) return;
                Label subscribeLabel = new Label("Add this club to your favourites?");
                subscribeLabel.setWrapText(true);
                Button yesButton = new Button("Yes");
                yesButton.getStyleClass().add(BLUE_BUTTON);
                yesButton.setOnAction(event1 -> addClubToFavourites(event1, invite));
                Button noButton = new Button("No");
                noButton.getStyleClass().add(BLUE_BUTTON);
                noButton.setOnAction(this::reload);
                hBox.setSpacing(10);
                hBox.getChildren().addAll(yesButton, noButton);
                hBox.setAlignment(Pos.CENTER);
                vBox.getChildren().clear();
                vBox.getChildren().addAll(subscribeLabel, hBox);
            } else {
                getInvites();
            }
        }
    }

    private VBox getParentVBox(HBox hBox) {
        Parent parent = hBox.getParent();
        if (parent instanceof VBox) {
            return (VBox) parent;
        }
        return null;
    }

    private void reload(ActionEvent actionEvent) {
        getInvites();
    }

    private void addClubToFavourites(ActionEvent event, InviteBean invite) {
        this.controller.addClubToFavourites(invite);
        reload(event);
    }

    private TitledPane createInvitePane(InviteBean inviteBean) {
        GraphicTournamentDetails tournamentDetailsController = new GraphicTournamentDetails();
        TitledPane tournamentPane = new TitledPane();
        tournamentPane.setAlignment(Pos.CENTER);
        TournamentBean tournamentBean = inviteBean.getTournament();
        tournamentPane.setText("You just got invited to a new tournament from " + tournamentBean.getClubName() + "\nThe invite expires on " + inviteBean.getExpiryDate());
        HBox hbox1 = new HBox();
        hbox1.setSpacing(20);
        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();
        VBox vbox3 = new VBox();
        hbox1.getChildren().addAll(vbox1, vbox2, vbox3);
        HBox hbox2 = new HBox();
        hbox2.setSpacing(10);
        VBox vbox4 = new VBox();
        vbox4.setAlignment(Pos.TOP_RIGHT);
        Button acceptButton = new Button("Accept");
        acceptButton.getStyleClass().add(BLUE_BUTTON);
        acceptButton.setOnAction(event -> acceptInvite(event, inviteBean));
        Button declineButton = new Button("Decline");
        declineButton.getStyleClass().add(BLUE_BUTTON);
        declineButton.setOnAction(event -> declineInvite(event, inviteBean));
        hbox2.getChildren().addAll(acceptButton, declineButton);
        vbox4.getChildren().add(hbox2);
        hbox1.getChildren().add(vbox4);
        List<Label> tournamentDetails = tournamentDetailsController.getTournamentDetails(tournamentBean);
        for (Label detail : tournamentDetails) {
            vbox1.getChildren().add(detail);
        }
        List<Label> clubDetails = tournamentDetailsController.getClubDetails(tournamentBean);
        for (Label detail : clubDetails) {
            vbox2.getChildren().add(detail);
        }
        List<Label> ownerDetails = tournamentDetailsController.getOwnerDetails(tournamentBean);
        for (Label detail : ownerDetails) {
            vbox3.getChildren().add(detail);
        }
        tournamentPane.setContent(hbox1);
        return tournamentPane;
    }

}
