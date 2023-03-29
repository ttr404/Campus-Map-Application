package org.uwo.cs2212;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uwo.cs2212.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;


public class MapEditingController {

    @FXML
    private Button save;
    @FXML
    private Button close;
    @FXML
    private Button addPOI;
    @FXML
    private Button deletePOI;
    @FXML
    private Button editPOI;
    @FXML
    private Button zoomIN;
    @FXML
    private Button zoomOUT;
    @FXML
    private Button floor0;
    @FXML
    private TextField searchText;
    @FXML
    private TextField poiName;
    @FXML
    private TextField roomNumber;
    @FXML
    private TextField RoomType;
    @FXML
    private TextField Description;
    @FXML
    private ListView POIList;
    @FXML
    private ScrollPane mapPane;

    private double zoom = 1.0;
    private double imageWidth;
    private double imageHeight;
    private FloorMap currentFloorMap;
    private BaseMap currentBaseMap;
    private MapConfig mapConfig;

    private void showMap(){
        try {
            URL mapUrl = CampusMapController.class.getResource(currentFloorMap.getMapFileName());
            URI uri = mapUrl.toURI();
            InputStream stream = new FileInputStream(new File(uri));
            Image image = new Image(stream);
            imageHeight = image.getHeight();
            imageWidth = image.getWidth();
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
    private void onZoomInButtonClick(ActionEvent actionEvent) {
        if (zoom > 0.5){
            zoom *= 0.8;
        }
        else{
            zoomIN.setDisable(false);
        }
        showMap();
    }

    @FXML
    private void onZoomOutButtonClick(ActionEvent actionEvent) {
        if (zoom < 1.7){
            zoom *= 1.2;
        }
        else{
            zoomOUT.setDisable(false);
        }
        showMap();
    }

    private void returnBack(String file,String title) throws IOException {
        int v = 1080;
        int v1 = 800;

        Stage stage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource(file));

        Scene scene = new Scene(fxmlLoader.load(), v, v1);

        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setX(200);
        stage.setY(70);
        stage.show();
    }
    public void setMapConfig(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    @FXML
    private void initialize() {
        // Load and display the map in the editing mode
        loadMapForEditing();
    }
    private void loadMapForEditing() {
        if (mapConfig != null) {
            // Find the desired FloorMap based on the baseMapName and floorMapName
            FloorMap desiredFloorMap = null;
            for (BaseMap baseMap : mapConfig.getBaseMaps()) {
                if (baseMap.getName().equals("baseMapName")) {
                    for (FloorMap floorMap : baseMap.getFloorMaps()) {
                        if (floorMap.getName().equals("floorMapName")) {
                            desiredFloorMap = floorMap;
                            break;
                        }
                    }
                    if (desiredFloorMap != null) {
                        break;
                    }
                }
            }
        }
    }

    public void onCloseButtonClick(ActionEvent actionEvent) {
        try {
            returnBack("main-view.fxml", "Western Campus Map");
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void onAddPOIButtonClick(ActionEvent actionEvent) {

    }
    public void onDeletePOIButtonClick(ActionEvent actionEvent) {

    }
    public void onEditPOIButtonClick(ActionEvent actionEvent) {

    }
}

