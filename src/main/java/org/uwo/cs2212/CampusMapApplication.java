package org.uwo.cs2212;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
        pressEnter(loginFxmlLoader, loginScene);
        stage.setTitle("Login page");
        stage.setScene(loginScene);
        stage.setResizable(false);
        //stage.initStyle(StageStyle.UNDECORATED);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void pressEnter(FXMLLoader loginFxmlLoader, Scene loginScene){
        LoginViewController controller = loginFxmlLoader.getController();
        loginScene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode().toString().equals("ENTER")) {
                    try {
                        controller.logIn();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }
}
