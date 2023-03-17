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
        LoginViewController.setStage(stage);
        FXMLLoader loginFxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource("login-view.fxml"));
        Scene loginScene = new Scene(loginFxmlLoader.load(), 571, 400);
        stage.setTitle("Login page");
        stage.setScene(loginScene);
        stage.setResizable(false);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}