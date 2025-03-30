module it.simonetagliaferri {
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires commons.codec;
    requires javafx.graphics;
    requires javafx.controls;

    opens it.simonetagliaferri.controller.graphic to javafx.graphics, javafx.fxml;
    opens it.simonetagliaferri.model.domain to com.google.gson;
    opens it.simonetagliaferri to javafx.fxml;
    exports it.simonetagliaferri;
}
