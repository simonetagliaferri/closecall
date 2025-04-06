package it.simonetagliaferri.controller.graphic.gui;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManagerGUI extends Application {

    private static Scene scene;

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        return new FXMLLoader(SceneManagerGUI.class.getResource("/view/gui/" + fxml + ".fxml")).load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("start"));
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/css/start.css")).toExternalForm());
        Group scalable = (Group) scene.lookup("#scalableContent");
        double baseWidth = 1280;
        double baseHeight = 720;
        stage.setMinWidth(baseWidth / 2);
        stage.setMinHeight(baseHeight / 2);
        bindScale(scene, scalable, baseWidth, baseHeight);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("CloseCall");
        stage.show();
    }

    private void bindScale(Scene scene, Group scalable, double baseWidth, double baseHeight) {
        // Listener for GUI scaling based on window size
        ChangeListener<Number> listener = (obs, oldVal, newVal) -> {
            double scaleX = scene.getWidth() / baseWidth;
            double scaleY = scene.getHeight() / baseHeight;
            double scale = Math.min(scaleX, scaleY);
            scalable.setScaleX(scale);
            scalable.setScaleY(scale);
        };

        scene.widthProperty().addListener(listener);
        scene.heightProperty().addListener(listener);
    }
}
