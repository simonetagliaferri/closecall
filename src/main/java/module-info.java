module it.simonetagliaferri {
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires javafx.graphics;
    requires javafx.controls;
    requires commons.validator;

    opens it.simonetagliaferri.model.domain to com.google.gson;
    opens it.simonetagliaferri to javafx.fxml;
    exports it.simonetagliaferri;
    opens it.simonetagliaferri.controller.graphic.gui to javafx.fxml, javafx.graphics;
}
