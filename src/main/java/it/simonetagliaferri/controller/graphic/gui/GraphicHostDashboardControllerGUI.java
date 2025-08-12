package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.TournamentBean;
import it.simonetagliaferri.exception.NavigationException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HostDashboardLogicController;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.Arrays;
import java.util.List;


public class GraphicHostDashboardControllerGUI extends GraphicController implements GUIController {

    private HostDashboardLogicController controller;

    @FXML private MenuButton account;
    @FXML private ToggleButton home;
    @FXML private ToggleButton newTournaments;
    @FXML private VBox contentWrapper;
    private List<ToggleButton> buttons;

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new HostDashboardLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getHostDAO(),
                appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void postInit() {
        Platform.runLater(() -> {
            HostBean hostBean = this.controller.getHostBean();
            account.setText(hostBean.getUsername());
            additionalInfo();
        });
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
        ToggleGroup tabs = new ToggleGroup();
        for (int i = 0; i < buttons.size(); i++) {
            ToggleButton button = buttons.get(i);
            button.setToggleGroup(tabs);
            button.setText("");
            button.setGraphic(icons.get(i));
            button.setMinHeight(40);
            button.setMaxHeight(40);
            button.setMinWidth(40);
            button.setMaxWidth(40);
        }
        tabs.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) {
                oldToggle.setSelected(true);
            }
        });
        buttons.get(0).setSelected(true);
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

    @FXML
    public void showAddClub() {
        SceneManagerGUI.loadWrapperWithContext("addClub", contentWrapper);
    }

    @FXML
    public void showAddTournament() {
        SceneManagerGUI.loadWrapperWithContext("addTournament", contentWrapper);
    }

    @FXML
    public void showInvitePlayers(TournamentBean tournamentBean) {
        GraphicInvitePlayersHostControllerGUI addPlayersFormController;
        addPlayersFormController = SceneManagerGUI.loadWrapperWithContext("invitePlayers", contentWrapper);
        addPlayersFormController.setTournamentBean(tournamentBean);
    }

    @FXML public void showHome() {
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
