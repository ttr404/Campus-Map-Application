package org.uwo.cs2212;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    private static Stage stage;

    public static void setStage(Stage stage) {
        LoginViewController.stage = stage;
    }

    public void onLoginGButtonClick(ActionEvent actionEvent) throws IOException {
        if (checkAccount()){
            FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1080, 720);
            stage.setTitle("Western Campus Map");
            stage.setScene(scene);
            stage.setResizable(false);
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
