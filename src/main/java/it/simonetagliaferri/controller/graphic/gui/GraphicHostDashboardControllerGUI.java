package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.infrastructure.AppContext;
import it.simonetagliaferri.beans.HostBean;
import it.simonetagliaferri.controller.graphic.GraphicController;
import it.simonetagliaferri.controller.logic.HostDashboardLogicController;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.Arrays;
import java.util.List;


public class GraphicHostDashboardControllerGUI extends GraphicController implements GUIController {
    private HostDashboardLogicController controller;
    private HostBean hostBean;


    @FXML private Text logo;
    @FXML private MenuButton account;
    @FXML private ToggleButton home;
    @FXML private ToggleButton newTournaments;
    @FXML private ToggleButton searchScreen;
    @FXML private VBox addTournamentWrapper;
    @FXML private AddTournamentFormController addTournamentsGUI;


    @Override
    public void initializeController(AppContext appContext) {
        this.navigationManager = appContext.getNavigationManager();
        this.controller = new HostDashboardLogicController(appContext.getSessionManager(), appContext.getDAOFactory().getTournamentDAO(),
                appContext.getDAOFactory().getHostDAO());
        postInit();
    }

    public void postInit() {
        hostBean = this.controller.getHostBean();
        account.setText(hostBean.getUsername());
    }

    @FXML
    private void initialize() {
        FontIcon icon1 = new FontIcon("oct-home-16");
        icon1.setIconSize(24);
        FontIcon icon2 = new FontIcon("oct-plus-16");
        icon2.setIconSize(24);
        FontIcon icon3 = new FontIcon("oct-search-16");
        icon3.setIconSize(24);
        List<FontIcon> icons = Arrays.asList(icon1, icon2, icon3);
        List<ToggleButton> buttons = Arrays.asList(home, newTournaments, searchScreen);
        setButtons(buttons, icons);
    }

    private void setButtons(List<ToggleButton> buttons, List<FontIcon> icons) {
        ToggleGroup tabs = new ToggleGroup();
        for (int i=0; i<buttons.size(); i++) {
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
                oldToggle.setSelected(true); // Re-select the old one
            }
        });
        buttons.get(0).setSelected(true);
        addTournamentWrapper.setVisible(false);
    }

    @FXML
    private void logout() {
        this.controller.logout();
        navigationManager.login();
    }

    @FXML
    private void changeScreen() {
        if (newTournaments.isSelected()) {
            addTournamentWrapper.setVisible(true);
            navigationManager.goToAddTournament();
        }
        else if (searchScreen.isSelected()) {
            addTournamentWrapper.setVisible(false);
        }
        else if (home.isSelected()) {
            addTournamentWrapper.setVisible(false);
        }
    }
}
