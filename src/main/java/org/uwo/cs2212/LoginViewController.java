package org.uwo.cs2212;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.uwo.cs2212.model.UserConfig;
import org.uwo.cs2212.model.UserList;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginViewController {
    @FXML
    private TextField loginName;
    @FXML
    private PasswordField password;
    @FXML
    private Label error;
    @FXML
    private ImageView weatherIcon;
    @FXML
    private ImageView temperature;
    @FXML
    private Button login;
    @FXML
    private Label weatherInfo;
    @FXML
    private Label tempLabel;
    private static Stage stage;

    public static void setStage(Stage stage) {
        LoginViewController.stage = stage;
    }

    public void initialize() {
        try {
            login.setPrefWidth(180);
            showWeather();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onLoginGButtonClick(ActionEvent actionEvent) throws IOException {
        logIn();
    }

    public void logIn() throws IOException {
        if (checkAccount()){
            FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage.setTitle("Western Campus Map");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setX(160);
            stage.setY(70);
            stage.show();
        }
        else{
            error.setText("Invalid user name or password.");
            password.setText("");
        }
    }

    public boolean checkAccount(){
        UserList userlist = ConfigUtil.loadUserList(CampusMapApplication.class.getResource("user-account.json"));
        String encodePassword = toHexString(getSHA(password.getText()));
        for(UserConfig userConfig:userlist.getAccountList()){
            if (loginName.getText().equals(userConfig.getUserName()) && encodePassword.equals(userConfig.getPassword())){
                return true;
            }
        }
        return false;
    }

    public void showWeather() throws IOException {
        Weather currWeather = new Weather(43.009953, -81.273613);
        currWeather.getWeather();
        ImageView imageView = currWeather.grabImage(currWeather.getIcon());
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        weatherIcon.setImage(imageView.getImage());

        weatherInfo.setText(currWeather.getWeather());

        ImageView temp;
        if (currWeather.getTemp() > 30.0){
            temp = new ImageView(getClass().getResource("thermometer-high.png").toExternalForm());
        } else if (currWeather.getTemp() < 0.0){
            temp = new ImageView(getClass().getResource("thermometer-low.png").toExternalForm());
        } else {
            temp = new ImageView(getClass().getResource("thermometer.png").toExternalForm());
        }
        temp.setFitWidth(16);
        temp.setFitWidth(16);
        temperature.setImage(temp.getImage());

        tempLabel.setText(currWeather.getTemp().toString() + "°C");
    }


    public static byte[] getSHA(String input)
    {
        // Static getInstance method is called with hashing SHA
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // digest() method called
        // to calculate message digest of an input
        // and return array of byte
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public static String toHexString(byte[] hash)
    {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64)
        {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }


}
