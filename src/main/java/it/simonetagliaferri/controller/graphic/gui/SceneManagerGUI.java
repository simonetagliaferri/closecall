package it.simonetagliaferri.controller.graphic.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
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
        double baseWidth = 1280;
        double baseHeight = 720;
        stage.setMinWidth(baseWidth / 2);
        stage.setMinHeight(baseHeight / 2);
        scene = new Scene(loadFXML("login"), baseWidth, baseHeight);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/view/css/start.css")).toExternalForm());
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("CloseCall");
        stage.show();
    }
}
