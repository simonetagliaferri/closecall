package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.JoinTournamentApplicationController;
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

    public static final String BLUE_BUTTON = "blue-button";
    private List<TitledPane> tournaments;
    private JoinTournamentApplicationController controller;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private Accordion tournamentList;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        FontIcon icon = new FontIcon("oct-search-16");
        icon.setIconSize(20);
        searchButton.setGraphic(icon);
        Platform.runLater(() -> this.controller = new JoinTournamentApplicationController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO(), appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getPlayerDAO()));
    }

    @FXML
    private void searchTournaments() {
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
                Label errorLabel = new Label("You are already in a team or you got invited.\nCheck your notifications.");
                vBox.getChildren().add(vBox.getChildren().indexOf(joinButton), errorLabel);
                joinButton.setDisable(true);
                joinButton.setVisible(false);
            } else {
                if (this.controller.isNotSubscribed(tournamentBean)) {
                    Label subscribeLabel = new Label("Do you want to add this club to your favourites?");
                    Button yesButton = new Button("Yes");
                    yesButton.getStyleClass().add(BLUE_BUTTON);
                    yesButton.setOnAction(event1 -> addClubToFavourites(event1, tournamentBean));
                    Button noButton = new Button("No");
                    noButton.getStyleClass().add(BLUE_BUTTON);
                    noButton.setOnAction(this::showConfirmation);
                    HBox hBox = new HBox();
                    hBox.setSpacing(10);
                    hBox.getChildren().addAll(yesButton, noButton);
                    hBox.setAlignment(Pos.CENTER);
                    vBox.getChildren().add(vBox.getChildren().indexOf(joinButton), subscribeLabel);
                    vBox.getChildren().remove(joinButton);
                    vBox.getChildren().add(hBox);
                } else {
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
        Label confirmLabel = new Label("You joined the tournament!\nJoin fees are covered by us for now.");
        if (parent instanceof VBox) {
            VBox vBox = (VBox) parent;
            vBox.getChildren().remove(button);
            vBox.getChildren().add(confirmLabel);
        } else if (parent instanceof HBox) {
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
        GraphicTournamentDetails tournamentDetailsController = new GraphicTournamentDetails();
        TitledPane tournamentPane = new TitledPane();
        tournamentPane.setText("Tournament " + tournamentBean.getTournamentName() + " hosted by " + tournamentBean.getClubName());
        HBox hbox1 = new HBox();
        hbox1.setSpacing(20);
        VBox vbox1 = new VBox();
        VBox vbox2 = new VBox();
        VBox vbox3 = new VBox();
        hbox1.getChildren().add(vbox1);
        hbox1.getChildren().add(vbox2);
        hbox1.getChildren().add(vbox3);
        HBox hbox2 = new HBox();
        VBox vbox4 = new VBox();
        Button joinButton = new Button("Join");
        joinButton.getStyleClass().add(BLUE_BUTTON);
        vbox4.getChildren().add(joinButton);
        vbox4.setAlignment(Pos.BOTTOM_RIGHT);
        hbox2.getChildren().add(vbox4);
        hbox1.getChildren().add(hbox2);
        if (tournamentBean.getAvailableSpots() > 0) {
            joinButton.setOnAction(event -> joinTournament(event, tournamentBean));
        } else {
            joinButton.setDisable(true);
            joinButton.setText("Too late");
        }
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
