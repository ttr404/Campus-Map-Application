module com.example.form {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens org.uwo.cs2212 to javafx.fxml;
    exports org.uwo.cs2212;
}