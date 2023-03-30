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

/**
 * The LoginViewController class is responsible for managing the login view.
 */
public class LoginViewController {
    @FXML
    private TextField loginName; // TextField for user to enter their username
    @FXML
    private PasswordField password; // PasswordField for user to enter their password
    @FXML
    private Label error; // Label to display error messages if the login fails
    @FXML
    private ImageView weatherIcon; // ImageView to display the current weather icon
    @FXML
    private ImageView temperature; // ImageView to display the current temperature icon
    @FXML
    private Button login; // Button to trigger the login process
    @FXML
    private Label weatherInfo; // Label to display the current weather description
    @FXML
    private Label tempLabel; // Label to display the current temperature value
    private static Stage stage; // The stage for the login view

    /**
     * Set the stage for the login view.
     *
     * @param stage a Stage object representing the stage for the login view
     */
    public static void setStage(Stage stage) {
        LoginViewController.stage = stage;
    }

    /**
     * Initialize the login view, set the button width, and show the current weather.
     *
     * @throws IOException if there is an error while attempting to display the weather
     */
    public void initialize() throws IOException {
        login.setPrefWidth(180);
        showWeather();
    }

    /**
     * Trigger the login process when the login button is clicked.
     *
     * @param actionEvent an ActionEvent object representing the click event
     * @throws IOException if there is an error while attempting to log in
     */
    public void onLoginGButtonClick(ActionEvent actionEvent) throws IOException {
        logIn();
    }

    /**
     * Handle the login process, show the main view if the login is successful, or display an error message if not.
     *
     * @throws IOException if there is an error while attempting to show the main view
     */
    public void logIn() throws IOException {
        if (checkAccount()) {
            FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1080, 800);

            CampusMapController campusMapController = fxmlLoader.getController();
            campusMapController.setAdmin(checkAdmin());

            stage.setTitle("Western Campus Map");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setX(200);
            stage.setY(70);
            stage.show();
        }
        // add multiple user function here
        else {
            error.setText("Invalid username or password.");
            password.setText("");
        }
    }

    /**
     * Check if the user entered the correct credentials.
     *
     * @return a boolean value representing whether the user entered the correct credentials
     */
    public boolean checkAccount() {
        UserList userlist = ConfigUtil.loadUserList(CampusMapApplication.class.getResource("user-account.json"));
        String encodePassword = toHexString(getSHA(password.getText()));

        // Check if the entered credentials match an account in the user list
        for (UserConfig userConfig : userlist.getAccountList()) {
            if (loginName.getText().equals(userConfig.getUsername()) && encodePassword.equals(userConfig.getPassword())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the user is an administrator.
     *
     * @return a boolean value representing whether the user is an administrator
     */
    public boolean checkAdmin() {
        UserList userlist = ConfigUtil.loadUserList(CampusMapApplication.class.getResource("user-account.json"));
        String encodePassword = toHexString(getSHA(password.getText()));
        if (loginName.getText().equals("admin")) {
            return true;
        }
        return false;
    }

    /**
     * Show the current weather on the login view.
     *
     * @throws IOException if there is an error while attempting to display the weather
     */
    public void showWeather() throws IOException {
        Weather currWeather = new Weather(43.009953, -81.273613);
        currWeather.getWeather();
        ImageView imageView = currWeather.grabImage(currWeather.getIcon());
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        weatherIcon.setImage(imageView.getImage());

        weatherInfo.setText(currWeather.getWeather());

        ImageView temp;
        if (currWeather.getTemp() > 30.0) {
            temp = new ImageView(getClass().getResource("thermometer-high.png").toExternalForm());
        } else if (currWeather.getTemp() < 0.0) {
            temp = new ImageView(getClass().getResource("thermometer-low.png").toExternalForm());
        } else {
            temp = new ImageView(getClass().getResource("thermometer.png").toExternalForm());
        }
        temp.setFitWidth(16);
        temp.setFitWidth(16);
        temperature.setImage(temp.getImage());

        tempLabel.setText(currWeather.getTemp().toString() + "°C");
    }

    /**
     * Calculate the SHA-256 hash of a given input string.
     *
     * @param input a String object representing the input to hash
     * @return a byte array representing the SHA-256 hash of the input
     */
    public static byte[] getSHA(String input) {
        // Static getInstance method is called with hashing SHA-256
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

    /**
     * Convert a byte array to a hexadecimal string.
     *
     * @param hash a byte array to convert to a hexadecimal string
     * @return a String object representing the hexadecimal value of the input byte array
     */
    public static String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 64) {
            hexString.insert(0, '0');
        }
        return hexString.toString();
    }
}