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
import javafx.scene.input.MouseEvent;
import org.uwo.cs2212.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Locale;
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
    private ListView informationList;
    @FXML
    private ScrollPane mapPane;

    private double zoom = 1.0;
    private double imageWidth;
    private double imageHeight;
    private PointOfInterest currentSelectedPoi;
    private MapConfig mapConfig;
    private BaseMap currentBaseMap;
    private FloorMap currentFloorMap;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapConfig = ConfigUtil.loadMapConfig(CampusMapApplication.class.getResource("map-config.json"));
        initializeMapSelector();
        initializeSearchListView();
        showFloorButtons();
        showMap();
        setFavouriteButtonState();

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

    private void initializeSearchListView(){
        MultipleSelectionModel<SearchResult> lvSelModel = informationList.getSelectionModel();
        lvSelModel.selectedItemProperty().addListener(
                (changed, oldVal, newVal) -> {
                    searchResultSelectionChanged(changed, oldVal, newVal);
                });
    }

    private void searchResultSelectionChanged(ObservableValue<? extends SearchResult> changed, SearchResult oldVal, SearchResult newVal) {
        selectPoi(newVal);
    }

    private void handleComboBoxValueChanged(ObservableValue ov, Object oldValue, Object newValue) {
        for(BaseMap baseMap: mapConfig.getBaseMaps()){
            if (newValue.toString().equals(baseMap.getName())){
                currentBaseMap = baseMap;
                currentFloorMap = currentBaseMap.getFloorMaps().get(0);
                showFloorButtons();
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
    private void onFloor1ButtonClick(ActionEvent actionEvent) {
        if (currentBaseMap.getFloorMaps().size() >= 2){
            currentFloorMap = currentBaseMap.getFloorMaps().get(1);
            showMap();
        }
    }

    @FXML
    private void onFloor2ButtonClick(ActionEvent actionEvent) {
        if (currentBaseMap.getFloorMaps().size() >= 3){
            currentFloorMap = currentBaseMap.getFloorMaps().get(2);
            showMap();
        }
    }

    @FXML
    private void onFloor3ButtonClick(ActionEvent actionEvent) {
        if (currentBaseMap.getFloorMaps().size() >= 4){
            currentFloorMap = currentBaseMap.getFloorMaps().get(3);
            showMap();
        }
    }

    @FXML
    private void onFloor4ButtonClick(ActionEvent actionEvent) {
        if (currentBaseMap.getFloorMaps().size() >= 5){
            currentFloorMap = currentBaseMap.getFloorMaps().get(4);
            showMap();
        }
    }

    private void showFloorButtons(){
        int floorCount = currentBaseMap.getFloorMaps().size();
        if (floorCount <= 1){
            floor0.setVisible(false);
            floor1.setVisible(false);
            floor2.setVisible(false);
            floor3.setVisible(false);
            floor4.setVisible(false);
        }
        else if (floorCount == 2){
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(false);
            floor3.setVisible(false);
            floor4.setVisible(false);
        }
        else if (floorCount == 3){
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(true);
            floor3.setVisible(false);
            floor4.setVisible(false);
        }
        else if (floorCount == 4){
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(true);
            floor3.setVisible(true);
            floor4.setVisible(false);
        }
        else{
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(true);
            floor3.setVisible(true);
            floor4.setVisible(true);
        }
    }

    public void onMapClicked(MouseEvent mouseEvent) {
        Point2D realMousePosition = calculateRealMousePosition(mouseEvent);
        for(Layer layer: currentFloorMap.getLayers()){
            for(PointOfInterest poi: layer.getPoints()) {
                if (hitTest(realMousePosition, poi)) {
                    selectPoi(new SearchResult(currentFloorMap, poi));
                    showPoiInList(poi);
                    return;
                }

            }
        }
        selectPoi(new SearchResult(currentFloorMap, null));
    }

    private void selectPoi(SearchResult searchResult){
        if(searchResult != null){
            currentFloorMap = searchResult.getFloorMap();
            if(currentSelectedPoi != null){
                currentSelectedPoi.setSelected(false);
            }
            currentSelectedPoi = searchResult.getPoi();
            if(currentSelectedPoi != null){
                currentSelectedPoi.setSelected(true);
            }
            showMap();
            setFavouriteButtonState();
        }
    }

    private void showPoiInList(PointOfInterest poi) {
        informationList.getItems().clear();
        informationList.getItems().add(new SearchResult(currentFloorMap, poi));
    }

    private boolean hitTest(Point2D mousePosition, PointOfInterest poi){
        if (mousePosition.getX() <= poi.getX()+6/zoom && mousePosition.getX() >= poi.getX()-6/zoom && mousePosition.getY() <= poi.getY()+6/zoom && mousePosition.getY() >= poi.getY()-6/zoom){
            return true;
        }
        return false;
    }

    private Point2D calculateRealMousePosition(MouseEvent mouseEvent){
        double windowXValue = (imageWidth - mapPane.getViewportBounds().getWidth()/zoom) * mapPane.getHvalue();
        double windowYValue = (imageHeight - mapPane.getViewportBounds().getHeight()/zoom) * mapPane.getVvalue();
        System.out.println("windowPosition:(" + windowXValue + ", " + windowYValue+")");
        double mouseX = windowXValue + mouseEvent.getX()/zoom;
        double mouseY = windowYValue + mouseEvent.getY()/zoom;
        if (mapPane.getViewportBounds().getHeight() >= imageHeight){
            mouseY = mouseEvent.getY()/zoom;
        }
        if (mapPane.getViewportBounds().getWidth() >= imageWidth){
            mouseX = mouseEvent.getX()/zoom;
        }
        System.out.println("mouse real position:(" + mouseX + ", " + mouseY+")");
        return new Point2D(mouseX, mouseY);
    }

    public void onSearchButtonClicked(ActionEvent actionEvent) {
        String text = searchText.getText().toLowerCase().trim();
        if (!text.equals("")){
            informationList.getItems().clear();
            for(FloorMap floorMap : currentBaseMap.getFloorMaps()){
                for (Layer layer: floorMap.getLayers()){
                    for(PointOfInterest poi : layer.getPoints()){
                        if (contains(poi.getName(), text) || contains(poi.getRoomNumber(), text)  || contains(poi.getType(), text)  || contains(poi.getDescription(), text)) {
                            informationList.getItems().add(new SearchResult(floorMap, poi));
                        }
                    }
                }
            }
        }
    }

    private static boolean contains(String target, String text){
        if(target != null && target.toLowerCase().contains(text)){
            return true;
        }
        return false;
    }

    private void setFavouriteButtonState() {
        ImageView imageView;
        if (currentSelectedPoi != null) {
            favoriteButton.setDisable(false);
            if (currentSelectedPoi.isFavorite()) {
                imageView = new ImageView(getClass().getResource("favorite1.png").toExternalForm());

            } else {
                imageView = new ImageView(getClass().getResource("favorite0.png").toExternalForm());
            }
        } else {
            imageView = new ImageView(getClass().getResource("favorite0.png").toExternalForm());
            favoriteButton.setDisable(true);
        }
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        favoriteButton.setGraphic(imageView);
    }

    public void onFavoriteButtonClicked(ActionEvent actionEvent) {
        if(currentSelectedPoi != null){
            currentSelectedPoi.setFavorite(!currentSelectedPoi.isFavorite());
            setFavouriteButtonState();
        }
    }

    public void onClearButtonClicked(ActionEvent actionEvent) {
        searchText.setText("");
        informationList.getItems().clear();
    }

    public void onListFavoritesButtonClicked(ActionEvent actionEvent) {
        informationList.getItems().clear();
        for (Layer layer: currentFloorMap.getLayers()){
            for(PointOfInterest poi : layer.getPoints()){
                if (poi.isFavorite()) {
                    informationList.getItems().add(poi);
                }
            }
        }
    }

}