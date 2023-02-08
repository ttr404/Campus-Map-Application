package org.uwo.cs2212;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapSelector.getItems().add("Western Main Campus");
        mapSelector.getItems().add("Middlesex College");
        mapSelector.getItems().add("Natural Science Centre");
        mapSelector.valueProperty().setValue("Western Main Campus");
    }
}