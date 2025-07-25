module it.simonetagliaferri {
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.apache.commons.codec;
    requires org.apache.commons.validator;
    requires java.desktop;
    requires mysql.connector.j;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.octicons;

    opens it.simonetagliaferri.model.domain to com.google.gson;
    opens it.simonetagliaferri to javafx.fxml;
    exports it.simonetagliaferri;
    exports it.simonetagliaferri.model.dao;
    exports it.simonetagliaferri.controller.graphic;
    exports it.simonetagliaferri.infrastructure.navigation;
    exports it.simonetagliaferri.model.domain;
    exports it.simonetagliaferri.model.invite;
    exports it.simonetagliaferri.controller.graphic.gui;
    opens it.simonetagliaferri.controller.graphic.gui to javafx.fxml, javafx.graphics;
    exports it.simonetagliaferri.model.strategy to com.google.gson;
    exports it.simonetagliaferri.infrastructure;
    opens it.simonetagliaferri.infrastructure to javafx.fxml, javafx.graphics;
    opens it.simonetagliaferri.infrastructure.navigation to javafx.fxml, javafx.graphics;
}
