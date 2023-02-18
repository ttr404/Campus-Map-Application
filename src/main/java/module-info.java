module com.example.form {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.desktop;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    opens org.uwo.cs2212 to javafx.fxml;
    exports org.uwo.cs2212;
    exports org.uwo.cs2212.model;
}