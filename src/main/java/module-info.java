module it.simonetagliaferri {
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.controls;
    requires org.apache.commons.codec;
    requires org.apache.commons.validator;
    requires mysql.connector.j;
    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    // add icon pack modules
    requires org.kordamp.ikonli.octicons;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.databind;
    requires java.sql;

    opens it.simonetagliaferri.model.invite to com.fasterxml.jackson.databind;
    opens it.simonetagliaferri.model.domain to com.fasterxml.jackson.databind;
    exports it.simonetagliaferri.model.dao.fs to com.fasterxml.jackson.databind;
    opens it.simonetagliaferri to javafx.fxml;
    exports it.simonetagliaferri;
    exports it.simonetagliaferri.model.dao;
    exports it.simonetagliaferri.controller.graphic;
    exports it.simonetagliaferri.infrastructure.navigation;
    exports it.simonetagliaferri.model.domain;
    exports it.simonetagliaferri.model.invite;
    exports it.simonetagliaferri.model.observer;
    exports it.simonetagliaferri.view.cli;
    exports it.simonetagliaferri.model.strategy;
    exports it.simonetagliaferri.controller.graphic.gui;
    exports it.simonetagliaferri.beans;
    opens it.simonetagliaferri.controller.graphic.gui to javafx.fxml, javafx.graphics;
    exports it.simonetagliaferri.infrastructure;
    opens it.simonetagliaferri.infrastructure to javafx.fxml, javafx.graphics;
    opens it.simonetagliaferri.infrastructure.navigation to javafx.fxml, javafx.graphics;
}
