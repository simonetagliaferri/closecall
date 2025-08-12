package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.PlayerBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.PlayerDashboardLogicController;
import it.simonetagliaferri.exception.NavigationException;
import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.infrastructure.SceneManagerGUI;
import it.simonetagliaferri.model.domain.Role;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuButton;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.javafx.FontIcon;
import java.util.Arrays;
import java.util.List;

public class GraphicPlayerDashboardControllerGUI extends GraphicController implements GUIController {

    PlayerDashboardLogicController controller;

    @FXML private VBox contentWrapper;
    @FXML private MenuButton account;
    @FXML private ToggleButton home;
    @FXML private ToggleButton joinTournament;
    @FXML private MenuButton notifications;


    @FXML
    private void initialize() {
        FontIcon icon1 = new FontIcon("oct-home-16");
        icon1.setIconSize(24);
        FontIcon icon2 = new FontIcon("oct-search-16");
        icon2.setIconSize(24);
        List<FontIcon> icons = Arrays.asList(icon1, icon2);
        List<ToggleButton> buttons = Arrays.asList(home, joinTournament);
        setButtons(buttons, icons);
        FontIcon icon3 = new FontIcon("oct-bell-16");
        icon3.setIconSize(16);
        notifications.setGraphic(icon3);
    }

    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new PlayerDashboardLogicController(appContext.getSessionManager(),
                appContext.getDAOFactory().getPlayerDAO(),
                appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getClubDAO());
        postInit();
    }

    public void postInit() {
        try {
            Platform.runLater(() -> {
                PlayerBean playerBean = this.controller.getPlayerBean();
                account.setText(playerBean.getUsername());
                navigationManager.goHome(Role.PLAYER);
            });
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
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
            if (joinTournament.isSelected()) {
                navigationManager.goToJoinTournament();
            } else if (home.isSelected()) {
                navigationManager.goHome(Role.PLAYER);
            }
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void showJoinTournament() {
        SceneManagerGUI.loadWrapperWithContext("joinTournament", contentWrapper);
    }

    public void showHome() {
        SceneManagerGUI.loadWrapperWithContext("playerHome", contentWrapper);
    }

    @FXML
    private void openNotifications() {
        try {
            navigationManager.goToNotifications(Role.PLAYER);
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void openInvites() {
        try {
            navigationManager.goToProcessInvites();
        } catch (NavigationException e) {
            new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
        }
    }

    public void showNotifications() {
        SceneManagerGUI.loadWrapperWithContext("playerNotifications", contentWrapper);
    }

    public void showInvites() {
        SceneManagerGUI.loadWrapperWithContext("playerInvites", contentWrapper);
    }

}
