package it.simonetagliaferri.controller.graphic.gui;

import it.simonetagliaferri.AppContext;
import it.simonetagliaferri.controller.graphic.GraphicController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class SceneManagerGUI extends Application {

    private static Scene scene;
    private static AppContext appContext = null;

    public static void setAppContext(AppContext context) {
        appContext = context;
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(SceneManagerGUI.class.getResource("/view/gui/" + fxml + ".fxml"));
        Parent root = fxmlLoader.load();
        GraphicController controller = fxmlLoader.getController();
        controller.setAppContext(appContext);
        return root;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
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
