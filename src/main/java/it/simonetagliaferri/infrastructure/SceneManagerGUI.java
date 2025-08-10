package it.simonetagliaferri.infrastructure;

import it.simonetagliaferri.controller.graphic.gui.GUIController;
import it.simonetagliaferri.controller.graphic.gui.GraphicHostDashboardControllerGUI;
import it.simonetagliaferri.controller.graphic.gui.GraphicPlayerDashboardControllerGUI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * It handles JavaFX scenes. Everything other than the start method is static since JavaFX internally instantiates SceneManagerGUI when Application.launch(SceneManagerGUI.class) is called,
 * and we can not directly reference that instance. The start method is not static since it's the method called by JavaFX after initializing its runtime.
 */
public class SceneManagerGUI extends Application {

    private static Scene scene;
    private static AppContext appContext = null;

    public static void setAppContext(AppContext context) {
        appContext = context;
    }

    /**
     * Used by graphic controllers to swap root nodes in the scene.
     */
    public static void setRoot(String fxml) throws IOException {
        FXMLLoader fxmlLoader = getLoader(fxml);
        scene.setRoot(loadFXML(fxmlLoader));
    }

    public static GraphicHostDashboardControllerGUI hostDashboard() {
        FXMLLoader fxmlLoader = getLoader("hostDashboard");
        try {
            scene.setRoot(loadFXML(fxmlLoader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader.getController();
    }

    public static GraphicPlayerDashboardControllerGUI playerDashboard() {
        FXMLLoader fxmlLoader = getLoader("playerDashboard");
        try {
            scene.setRoot(loadFXML(fxmlLoader));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fxmlLoader.getController();
    }

    /**
     * It gets the correct fxml file from the parameter, it then loads it so that we can get the controller of that fxml, so that we can use setAppContext to pass
     * the app context to the graphic controller. After that we return root to the caller, which will use it to set the scene's root.
     */
    private static Parent loadFXML(FXMLLoader fxmlLoader) throws IOException {
        Parent root = fxmlLoader.load();
        GUIController controller = fxmlLoader.getController();
        controller.initializeController(appContext);
        return root;
    }

    public static FXMLLoader getLoader(String fxml) {
        return new FXMLLoader(SceneManagerGUI.class.getResource("/hostView/gui/" + fxml + ".fxml"));
    }

    public static <T extends GUIController> T getController(FXMLLoader fxmlLoader) {
        return fxmlLoader.getController();
    }

    public static Node getRoot(FXMLLoader fxmlLoader) throws IOException {
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    /**
     * The start method is not static because it's the method called by JavaFX after initializing its runtime.
     */
    @Override
    public void start(Stage stage) throws IOException {
        /*
         * These two properties are set to fix blurry text.
         */
        System.setProperty("prism.lcdtext", "false");
        System.setProperty("prism.text", "t2k");
        double baseWidth = 1280;
        double baseHeight = 720;
        stage.setMinWidth(baseWidth / 2);
        stage.setMinHeight(baseHeight / 2);
        FXMLLoader loader = getLoader("login");
        Scene s = new Scene(loadFXML(loader), baseWidth, baseHeight);
        s.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/hostView/css/start.css")).toExternalForm());
        stage.setScene(s);
        stage.centerOnScreen();
        stage.setTitle("CloseCall");
        stage.show();
        setScene(s);
    }

    public static <T extends GUIController> T loadWrapperWithContext(String fxml, VBox contentWrapper) throws IOException {
        FXMLLoader loader = SceneManagerGUI.getLoader(fxml);
        Node root = SceneManagerGUI.getRoot(loader);
        T controller = SceneManagerGUI.getController(loader);
        controller.initializeController(appContext); // optional
        contentWrapper.getChildren().setAll(root);
        return controller;
    }

    private static void setScene(Scene s) {
        scene = s;
    }

}
