package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.beans.UserBean;
import it.simonetagliaferri.controller.graphic.SessionManager;
import it.simonetagliaferri.controller.logic.HostDashboardController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.Text;

public class GraphicHostDashboardControllerGUI {
    @FXML
    private Text text;
    @FXML
    private Button logoutButton;
    private final UserBean user = SessionManager.getCurrentUser();
    private final HostDashboardController controller = new HostDashboardController();

    @FXML
    public void initialize() {
        text.setText("Hello " + user.getUsername());
    }

    @FXML
    private void logout() {
        this.controller.logout();
    }

}
