module tesler {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires spring.context;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires java.sql;
    requires java.xml.bind;
    requires spring.beans;
    requires spring.core;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;

    opens applications;
    opens controllers;
    opens resources;
    opens entitys;
    opens configs;
    opens DAO;
}