package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.ClubBean;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.controller.logic.HostDashboardApplicationController;
import it.simonetagliaferri.exception.NavigationException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class GraphicHostDashboardControllerGUI extends GraphicDashboardControllerGUI {

    private HostDashboardApplicationController controller;

    @FXML
    private MenuButton clubDetails;
    @FXML
    private MenuButton account;
    @FXML
    private ToggleButton home;
    @FXML
    private ToggleButton newTournaments;
    @FXML
    private HBox contentWrapper;
    @FXML
    private FontIcon notificationBell;
    private List<ToggleButton> buttons;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new HostDashboardApplicationController(appContext.getSessionManager(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void postInit() {
        Platform.runLater(() -> {
            HostBean hostBean = this.controller.getHostBean();
            account.setText(hostBean.getUsername());
            additionalInfo();
            ClubBean clubBean = this.controller.getClubBean();
            if (clubBean != null) {
                String clubName = this.controller.getClubBean().getName();
                clubDetails.setText(clubName);
                setClubDetails();
                clubDetails.setVisible(true);
            }
        });
    }

    private List<String> getClubDetails() {
        ClubBean clubBean = this.controller.getClubBean();
        List<String> details = new ArrayList<>();
        String clubName = "Club name: " + clubBean.getName();
        details.add(clubName);
        String clubStreet = "Club street: " + clubBean.getStreet();
        details.add(clubStreet);
        String clubNumber = "Club number: " + clubBean.getNumber();
        details.add(clubNumber);
        String clubCity = "Club city: " + clubBean.getCity();
        details.add(clubCity);
        String clubZip = "Club zip code: " + clubBean.getZip();
        details.add(clubZip);
        String clubState = "Club state: " + clubBean.getState();
        details.add(clubState);
        String clubCountry = "Club country: " + clubBean.getCountry();
        details.add(clubCountry);
        String clubPhone = "Club phone: " + clubBean.getPhone();
        details.add(clubPhone);
        return details;
    }

    private void setClubDetails() {
        List<String> details = getClubDetails();
        for (String detail : details) {
            MenuItem item = new MenuItem(detail);
            item.getStyleClass().add("fake-menu-item");
            clubDetails.getItems().add(item);
        }
    }

    @FXML
    private void initialize() {
        FontIcon icon1 = new FontIcon("oct-home-16");
        icon1.setIconSize(24);
        FontIcon icon2 = new FontIcon("oct-plus-16");
        icon2.setIconSize(24);
        List<FontIcon> icons = Arrays.asList(icon1, icon2);
        buttons = Arrays.asList(home, newTournaments);
        setButtons(buttons, icons);
    }

    private void additionalInfo() {
        try {
            if (this.controller.additionalInfoNeeded()) {
                notificationBell.setDisable(true);
                disableButtons(buttons);
                navigationManager.goToAddClub();
            } else {
                navigationManager.goHome(Role.HOST);
            }
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    private void disableButtons(List<ToggleButton> buttons) {
        for (ToggleButton button : buttons) {
            button.setDisable(true);
        }
    }

    private void setButtons(List<ToggleButton> buttons, List<FontIcon> icons) {
        setDashboardButtons(buttons, icons);
    }

    @FXML
    private void logout() {
        try {
            this.controller.logout();
            navigationManager.login();
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void changeScreen() {
        try {
            if (newTournaments.isSelected()) {
                navigationManager.goToAddTournament();
            } else if (home.isSelected()) {
                navigationManager.goHome(Role.HOST);
            }
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void showAddClub() {
        SceneManagerGUI.loadWrapperWithContext("addClub", contentWrapper);
    }

    public void showAddTournament() {
        SceneManagerGUI.loadWrapperWithContext("addTournament", contentWrapper);
    }

    public void showInvitePlayers(TournamentBean tournamentBean) {
        GraphicInvitePlayersHostControllerGUI addPlayersFormController;
        addPlayersFormController = SceneManagerGUI.loadWrapperWithContext("invitePlayers", contentWrapper);
        addPlayersFormController.setTournamentBean(tournamentBean);
    }

    public void showHome() {
        SceneManagerGUI.loadWrapperWithContext("hostHome", contentWrapper);
    }

    @FXML
    private void openNotifications() {
        try {
            navigationManager.goToNotifications(Role.HOST);
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void showNotifications() {
        SceneManagerGUI.loadWrapperWithContext("hostNotifications", contentWrapper);
    }

}
