package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.InviteBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.ProcessPlayerInviteLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.model.invite.InviteStatus;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;

public class GraphicProcessPlayerInviteControllerGUI extends GraphicController implements GUIController {

    private ProcessPlayerInviteLogicController controller;

    @FXML
    private VBox inviteList;

    @Override
    public void initializeController(AppContext appContext) {
        this.controller = new ProcessPlayerInviteLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(), appContext.getDAOFactory().getHostDAO(), appContext.getDAOFactory().getClubDAO());
        getInvites();
    }

    private void getInvites() {
        List<InviteBean> invites = controller.getInvites();
        for (InviteBean invite : invites) {
            showInvite(invite);
        }
    }

    private void showInvite(InviteBean invite) {
        if (!this.controller.expiredInvite(invite)) {
            TournamentBean tournament = invite.getTournament();
            ClubBean club = tournament.getClub();
            VBox vBox = new VBox();
            HBox hBox = new HBox();
            Label inviteName = new Label("You just got invited to a new tournament from " + club.getName());
            Label inviteExpiryDate = new Label("The invite expires on" + invite.getExpiryDate());
            Button acceptButton = new Button("Accept");
            acceptButton.setOnAction(event -> acceptInvite(invite));
            Button declineButton = new Button("Decline");
            declineButton.setOnAction(event -> declineInvite(invite));
            List<Label> tournamentDetails = getTournamentDetails(tournament);
            vBox.getChildren().addAll(inviteName, inviteExpiryDate);
            vBox.getChildren().addAll(tournamentDetails);
            hBox.getChildren().addAll(vBox, acceptButton, declineButton);
            inviteList.getChildren().add(hBox);
        }
    }

    private void acceptInvite(InviteBean invite) {
        this.controller.updateInvite(invite, InviteStatus.ACCEPTED);
    }

    private void declineInvite(InviteBean invite) {
        this.controller.updateInvite(invite, InviteStatus.DECLINED);
    }

    private List<Label> getTournamentDetails(TournamentBean tournamentBean) {
        List<Label> details = new ArrayList<>();
        Label tournamentDetails = new Label("Tournament details:");
        details.add(tournamentDetails);
        Label tournamentName = new Label("Tournament name:");
        details.add(tournamentName);
        Label tournamentNameChild = new Label(tournamentBean.getTournamentName());
        details.add(tournamentNameChild);
        Label tournamentFormat = new Label("Tournament format:");
        details.add(tournamentFormat);
        Label tournamentFormatChild = new Label(tournamentBean.getTournamentFormat());
        details.add(tournamentFormatChild);
        Label tournamentType = new Label("Tournament type:");
        details.add(tournamentType);
        Label tournamentTypeChild = new Label(tournamentBean.getTournamentType());
        details.add(tournamentTypeChild);
        Label courtType = new Label("Court type:");
        details.add(courtType);
        Label courtTypeChild = new Label(tournamentBean.getCourtType());
        details.add(courtTypeChild);
        Label teamNumber = new Label("Number of teams:");
        details.add(teamNumber);
        Label teamNumberChild = new Label(Integer.toString(tournamentBean.getTeamsNumber()));
        details.add(teamNumberChild);
        Label availableSpots = new Label("Available spots:");
        details.add(availableSpots);
        Label availableSpotsChild = new Label(Integer.toString(tournamentBean.getAvailableSpots()));
        details.add(availableSpotsChild);
        Label joinFees = new Label("Join fee:");
        details.add(joinFees);
        Label joinFeesChild = new Label(Double.toString(tournamentBean.getJoinFee()));
        details.add(joinFeesChild);
        Label courtCost = new Label("Court cost:");
        details.add(courtCost);
        Label courtCostChild = new Label(Double.toString(tournamentBean.getCourtPrice()));
        details.add(courtCostChild);
        Label startDate = new Label("Start date:");
        details.add(startDate);
        Label startDateChild = new Label(String.valueOf(tournamentBean.getStartDate()));
        details.add(startDateChild);
        Label estimatedEndDate = new Label("Estimated end date:");
        details.add(estimatedEndDate);
        Label estimatedEndDateChild = new Label(String.valueOf(tournamentBean.getEndDate()));
        details.add(estimatedEndDateChild);
        Label signupDeadline = new Label("Signup deadline:");
        details.add(signupDeadline);
        Label signupDeadlineChild = new Label(String.valueOf(tournamentBean.getSignupDeadline()));
        details.add(signupDeadlineChild);
        return details;
    }

}
