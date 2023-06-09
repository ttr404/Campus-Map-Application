package org.uwo.cs2212;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.IOException;

/**
 * The CampusMapApplication class is responsible for launching the application and setting up the login view.
 * This class extends the JavaFX Application class.
 *
 * @author Yaopeng Xie
 * @author Binchi Zhang
 */
public class CampusMapApplication extends Application {

    /**
     * Set up the login view and display it.
     *
     * @param stage A Stage object representing the application window
     * @throws IOException If there is an error while attempting to load the login view
     */
    @Override
    public void start(Stage stage) throws IOException {

        LoginViewController.setStage(stage);
        FXMLLoader loginFxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource("login-view.fxml"));
        Scene loginScene = new Scene(loginFxmlLoader.load(), 570, 400);
        pressEnter(loginFxmlLoader, loginScene);
        stage.setTitle("Login");
        stage.setScene(loginScene);
        stage.setResizable(false);

        Image icon = new Image(getClass().getResourceAsStream("western-logo.png"));
        stage.getIcons().add(icon);

        stage.show();
    }

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * Look for the Enter key to be pressed while the login view is active.
     *
     * @param loginFxmlLoader A FXMLLoader object representing the loader for the login view FXML file
     * @param loginScene      A Scene object representing the login view scene
     */
    public static void pressEnter(FXMLLoader loginFxmlLoader, Scene loginScene) {
        LoginViewController controller = loginFxmlLoader.getController();

        loginScene.setOnKeyPressed(new EventHandler<>() {
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
