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

    requires java.sql;
    requires java.desktop;

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
    exports it.simonetagliaferri.infrastructure;
    exports it.simonetagliaferri.controller.logic;
    exports it.simonetagliaferri.model.dao.fs;
    exports it.simonetagliaferri.model.dao.jdbc;
    exports it.simonetagliaferri.utils;
    exports it.simonetagliaferri.exception;
    opens it.simonetagliaferri.controller.graphic.gui to javafx.fxml, javafx.graphics;
    opens it.simonetagliaferri.infrastructure to javafx.fxml, javafx.graphics;
    opens it.simonetagliaferri.infrastructure.navigation to javafx.fxml, javafx.graphics;

}
