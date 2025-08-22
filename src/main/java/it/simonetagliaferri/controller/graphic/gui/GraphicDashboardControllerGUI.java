package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.controller.graphic.GraphicController;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import org.kordamp.ikonli.javafx.FontIcon;

import java.util.List;

public abstract class GraphicDashboardControllerGUI extends GraphicController implements GUIController {

    protected void setDashboardButtons(List<ToggleButton> buttons, List<FontIcon> icons) {
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
}
