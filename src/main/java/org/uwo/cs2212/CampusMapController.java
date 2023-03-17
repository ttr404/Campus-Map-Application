package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ser.Serializers;
import javafx.beans.value.ChangeListener;
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
import org.uwo.cs2212.model.BaseMap;
import org.uwo.cs2212.model.FloorMap;
import org.uwo.cs2212.model.Layer;
import org.uwo.cs2212.model.MapConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Map;
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

    private double zoom = 1.0;
    private MapConfig mapConfig;
    private BaseMap currentBaseMap;
    private FloorMap currentFloorMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapConfig = ConfigUtil.loadMapConfig(CampusMapApplication.class.getResource("map-config.json"));
        initializeMapSelector();
        showMap();

    }

    private void initializeMapSelector() {
        for(BaseMap baseMap: mapConfig.getBaseMaps()){
            mapSelector.getItems().add(baseMap.getName());
        }
        currentBaseMap = mapConfig.getBaseMaps().get(0);
        currentFloorMap = currentBaseMap.getFloorMaps().get(0);
        mapSelector.valueProperty().setValue(currentBaseMap.getName());
        mapSelector.valueProperty().addListener((ov, oldValue, newValue) -> {
            handleComboBoxValueChanged(ov, oldValue, newValue);
        });
    }

    private void handleComboBoxValueChanged(ObservableValue ov, Object oldValue, Object newValue) {
        for(BaseMap baseMap: mapConfig.getBaseMaps()){
            if (newValue.toString().equals(baseMap.getName())){
                currentBaseMap = baseMap;
                currentFloorMap = currentBaseMap.getFloorMaps().get(0);
                showMap();
            }
		}
    }

    private void showMap(){
        try {
            URL mapUrl = CampusMapController.class.getResource(currentFloorMap.getMapFileName());
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
            imageView.setFitWidth(image.getWidth() * zoom);
            imageView.setPreserveRatio(true);
            root.getChildren().add(imageView);
            for(Layer layer: currentFloorMap.getLayers()){
                ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, layer);
                root.getChildren().add(imageLayer);
            }
            mapPane.setContent(root);
        }
        catch(Exception ex)
        {}
    }

    @FXML
    private void onSettingsButtonClicked(ActionEvent actionEvent) {
        System.out.println("operate successful.");
    }

    @FXML
    private void onHelpButtonClicked(ActionEvent actionEvent) {
        try {
            URL configUrl = getClass().getResource("help.pdf");
            Desktop desk = Desktop.getDesktop();
            desk.browse(configUrl.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onZoomInButtonClicked(ActionEvent actionEvent) {
        zoom *= 0.8;
        showMap();
    }

    @FXML
    private void onZoomOutButtonClicked(ActionEvent actionEvent) {
        zoom *= 1.2;
        showMap();
    }

    @FXML
    private void onFloorGButtonClick(ActionEvent actionEvent) {
        currentFloorMap = currentBaseMap.getFloorMaps().get(0);
        showMap();
    }

    @FXML
    private void onFloor2ButtonClick(ActionEvent actionEvent) {
        if (currentBaseMap.getFloorMaps().size() >= 2){
            currentFloorMap = currentBaseMap.getFloorMaps().get(1);
            showMap();
        }
    }
}