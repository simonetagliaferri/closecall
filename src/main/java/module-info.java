module it.simonetagliaferri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens it.simonetagliaferri to javafx.fxml;
    exports it.simonetagliaferri;
}
