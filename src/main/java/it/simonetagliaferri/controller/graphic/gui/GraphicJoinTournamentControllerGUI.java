package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.JoinTournamentLogicController;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.view.cli.JoinTournamentView;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.List;

public class GraphicJoinTournamentControllerGUI extends GraphicController implements GUIController {

    private JoinTournamentLogicController controller;
    @FXML private Button searchButton;
    @FXML private TextField searchField;
    @FXML private Accordion tournamentList;
    List<TitledPane> tournaments;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        FontIcon icon = new FontIcon("oct-search-16");
        icon.setIconSize(24);
        searchButton.setGraphic(icon);
        Platform.runLater(() -> this.controller = new JoinTournamentLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO(), appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO()));
    }

    @FXML private void searchTournaments() {
        tournamentList.getPanes().clear();
        List<TournamentBean> tournamentBeans = this.controller.searchTournament(searchField.getText());
        for (TournamentBean tournamentBean : tournamentBeans) {
            if (tournaments == null) tournaments = new ArrayList<>();
            tournaments.clear();
            TitledPane tournament = createTournamentPane(tournamentBean);
            tournamentList.getPanes().add(tournament);
        }
    }

    private void joinTournament(ActionEvent event, TournamentBean tournamentBean) {
        Button joinButton = (Button) event.getSource();
        Parent parent = joinButton.getParent();
        VBox vBox;
        if (parent instanceof VBox) {
            vBox = (VBox) parent;
            if (this.controller.joinTournament(tournamentBean) == JoinTournamentView.JoinError.ALREADY_IN_A_TEAM) {
                Label errorLabel = new Label("You are already in a team or you got invited. Check your notifications.");
                vBox.getChildren().add(vBox.getChildren().indexOf(joinButton), errorLabel);
                joinButton.setDisable(true);
            }
            else {
                if (!this.controller.isSubscribed(tournamentBean)) {
                    Label subscribeLabel = new Label("Do you want to add this club to your favourites?");
                    Button yesButton = new Button("Yes");
                    yesButton.setOnAction(event1 -> addClubToFavourites(event1, tournamentBean));
                    Button noButton = new Button("No");
                    noButton.setOnAction(this::showConfirmation);
                    HBox hBox = new HBox();
                    hBox.getChildren().addAll(yesButton, noButton);
                    hBox.setAlignment(Pos.CENTER);
                    vBox.getChildren().add(vBox.getChildren().indexOf(joinButton), subscribeLabel);
                    vBox.getChildren().remove(joinButton);
                    vBox.getChildren().add(hBox);
                }
                else {
                    showConfirmation(event);
                }
            }
        }
    }

    private void addClubToFavourites(ActionEvent event, TournamentBean tournamentBean) {
        this.controller.addClubToFavourites(tournamentBean);
        showConfirmation(event);
    }

    private void showConfirmation(ActionEvent event) {
        Button button = (Button) event.getSource();
        Parent parent = button.getParent();
        Label confirmLabel = new Label("You joined the tournament! Join fees are covered by us for now.");
        if (parent instanceof VBox) {
            VBox vBox = (VBox) parent;
            vBox.getChildren().remove(button);
            vBox.getChildren().add(confirmLabel);
        }
        else if (parent instanceof HBox) {
            HBox hBox = (HBox) parent;
            Parent parent1 = hBox.getParent();
            if (parent1 instanceof VBox) {
                VBox vBox = (VBox) parent1;
                vBox.getChildren().removeAll(vBox.getChildren());
                vBox.getChildren().add(confirmLabel);
            }
        }
    }


    private TitledPane createTournamentPane(TournamentBean tournamentBean) {
        TitledPane tournamentPane = new TitledPane();
        tournamentPane.setText("Tournament " + tournamentBean.getTournamentName() + " hosted by " + tournamentBean.getClub().getName());
        HBox hbox1 = new HBox();
        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();
        VBox vbox3 = new VBox();
        hbox1.getChildren().add(vbox1);
        hbox1.getChildren().add(vbox2);
        hbox1.getChildren().add(vbox3);
        if (tournamentBean.getAvailableSpots() > 0) {
            HBox hbox2 = new HBox();
            VBox vbox4 = new VBox();
            Button joinButton = new Button("Join");
            joinButton.getStyleClass().add("blue-button");
            joinButton.setOnAction(event -> joinTournament(event, tournamentBean));
            vbox4.getChildren().add(joinButton);
            vbox4.setAlignment(Pos.BOTTOM_RIGHT);
            hbox2.getChildren().add(vbox4);
            hbox1.getChildren().add(hbox2);
        }
        List<Label> tournamentDetails = getTournamentDetails(tournamentBean);
        for (Label detail : tournamentDetails) {
            vbox1.getChildren().add(detail);
        }
        List<Label> clubDetails = getClubDetails(tournamentBean);
        for (Label detail : clubDetails) {
            vbox2.getChildren().add(detail);
        }
        List<Label> ownerDetails = getOwnerDetails(tournamentBean);
        for (Label detail : ownerDetails) {
            vbox3.getChildren().add(detail);
        }
        tournamentPane.setContent(hbox1);
        return tournamentPane;
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

    private List<Label> getClubDetails(TournamentBean tournamentBean) {
        List<Label> details = new ArrayList<>();
        Label clubDetails = new Label("Club details:");
        details.add(clubDetails);
        Label clubName = new Label("Club name:");
        details.add(clubName);
        Label clubNameChild = new Label(tournamentBean.getClub().getName());
        details.add(clubNameChild);
        Label clubStreet = new Label("Club street:");
        details.add(clubStreet);
        Label clubStreetChild = new Label(tournamentBean.getClub().getStreet());
        details.add(clubStreetChild);
        Label clubNumber = new Label("Club number:");
        details.add(clubNumber);
        Label clubNumberChild = new Label(tournamentBean.getClub().getNumber());
        details.add(clubNumberChild);
        Label clubCity = new Label("Club city:");
        details.add(clubCity);
        Label clubCityChild = new Label(tournamentBean.getClub().getCity());
        details.add(clubCityChild);
        Label clubZip = new Label("Club zip code:");
        details.add(clubZip);
        Label clubZipChild = new Label(tournamentBean.getClub().getZip());
        details.add(clubZipChild);
        Label clubState = new Label("Club state:");
        details.add(clubState);
        Label clubStateChild = new Label(tournamentBean.getClub().getState());
        details.add(clubStateChild);
        Label clubCountry = new Label("Club country:");
        details.add(clubCountry);
        Label clubCountryChild = new Label(tournamentBean.getClub().getCountry());
        details.add(clubCountryChild);
        Label clubPhone = new Label("Club phone:");
        details.add(clubPhone);
        Label clubPhoneChild = new Label(tournamentBean.getClub().getPhone());
        details.add(clubPhoneChild);
        return details;
    }

    private List<Label> getOwnerDetails(TournamentBean tournamentBean) {
        List<Label> details = new ArrayList<>();
        Label clubOwnerDetails = new Label("Club owner details:");
        details.add(clubOwnerDetails);
        Label clubOwnerName = new Label("Name:");
        details.add(clubOwnerName);
        Label clubOwnerNameChild = new Label(tournamentBean.getClub().getOwner().getUsername());
        details.add(clubOwnerNameChild);
        Label clubOwnerEmail = new Label("Email:");
        details.add(clubOwnerEmail);
        Label clubOwnerEmailChild = new Label(tournamentBean.getClub().getOwner().getEmail());
        details.add(clubOwnerEmailChild);
        return details;
    }

}
