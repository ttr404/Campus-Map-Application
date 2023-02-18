package org.uwo.cs2212;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.uwo.cs2212.model.MapConfig;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class CampusMapApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
        stage.setTitle("Western Campus Map");

        URL css = getClass().getResource("/org/uwo/cs2212/stylesheet.css");
        scene.getStylesheets().add(css.toExternalForm());

        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}