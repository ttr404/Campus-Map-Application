package org.uwo.cs2212;

import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.awt.*;

public class CampusMapController implements Initializable {
    @FXML
    private Button floor0;
    @FXML
    private Button floor1;
    @FXML
    private Button floor2;
    @FXML
    private Button floor3;
    @FXML
    private Button floor4;
    @FXML
    private Button floor5;
    @FXML
    private Button poiToggleButton;
    @FXML
    private Button favoriteButton;
    @FXML
    private Button zoomIn;
    @FXML
    private Button zoomOut;
    @FXML
    private Button settingsButton;
    @FXML
    private ComboBox mapSelector;
    @FXML
    private TextField searchText;
    @FXML
    private ListView searchResult;
    @FXML
    private ScrollPane mapPane;
    @FXML
    protected void onHelloButtonClick() {
    }
    private String mapName = "main-map.png";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeMapSelector();
        showMap();

    }

    private void initializeMapSelector() {
        mapSelector.getItems().add("Western Main Campus");
        mapSelector.getItems().add("Middlesex College");
        mapSelector.getItems().add("Natural Science Centre");
        mapSelector.valueProperty().setValue("Western Main Campus");
        mapSelector.valueProperty().addListener((ov, oldValue, newValue) -> {
            handleComboBoxValueChanged(ov, oldValue, newValue);
        });
    }

    private void handleComboBoxValueChanged(ObservableValue ov, Object oldValue, Object newValue) {
        if (newValue.toString().equals("Western Main Campus")){

        }
        else if (newValue.toString().equals("Middlesex College")){
            mapName = "middlesex-f0.png";
            showMap();
        }
        else if (newValue.toString().equals("Natural Science Centre")){
            
        }
        else{

        }
    }

    private void showMap(){
        try {
            URL mapUrl = CampusMapController.class.getResource(mapName);
            URI uri = mapUrl.toURI();
            InputStream stream = new FileInputStream(new File(uri));
            Image image = new Image(stream);

            //Creating the image view
            ImageView imageView = new ImageView();
            //Setting image to the image view
            imageView.setImage(image);
            //Setting the image view parameters
            imageView.setX(0);
            imageView.setY(0);
            Group root = new Group();
            imageView.setFitWidth(image.getWidth());
            imageView.setPreserveRatio(true);
            root.getChildren().add(imageView);
            mapPane.setContent(root);
        }
        catch(Exception ex)
        {}
    }

    public void onSettingsButtonClicked(ActionEvent actionEvent) {
        System.out.println("operate successful.");
    }

    public void onHelpButtonClicked(ActionEvent actionEvent) {
        try {
            URL configUrl = getClass().getResource("help.pdf");
            Desktop desk = Desktop.getDesktop();
            desk.browse(configUrl.toURI());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}