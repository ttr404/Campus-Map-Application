package org.uwo.cs2212;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    private TextField poiName;
    @FXML
    private TextField roomNumber;
    @FXML
    private TextField RoomType;
    @FXML
    private TextField Description;
    @FXML
    private ComboBox<String> roomSelector;
    @FXML
    private ScrollPane mapPane;
    private double zoom = 1.0;
    private FloorMap currentFloorMap;

    /**
     * Handles the event when the "Zoom In" button is clicked in the UI.
     *
     * This method updates the zoom level of the campus map and displays the updated map.
     * If the current zoom level is already at its maximum (0.5), the "Zoom In" button is disabled.
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    @FXML
    private void onZoomInButtonClick(ActionEvent actionEvent) {
        // Check if zoom level is greater than 0.5
        if (zoom > 0.5){
            // Reduce zoom level by a factor of 0.8
            zoom *= 0.8;
        }
        else{
            // Disable "Zoom In" button if current zoom level is at maximum
            zoomIN.setDisable(false);
        }

        // Create a new CampusMapController object and call the showMap method to display the updated map
        CampusMapController campusMapController = new CampusMapController();
        campusMapController.showMap();
    }


    /**
     * Handles the event when the "Zoom Out" button is clicked in the UI.
     *
     * This method updates the zoom level of the campus map and displays the updated map.
     * If the current zoom level is already at its minimum (1.7), the "Zoom Out" button is disabled.
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    @FXML
    private void onZoomOutButtonClick(ActionEvent actionEvent) {
        // Check if zoom level is less than 1.7
        if (zoom < 1.7){
            // Increase zoom level by a factor of 1.2
            zoom *= 1.2;
        }
        else{
            // Disable "Zoom Out" button if current zoom level is at minimum
            zoomOUT.setDisable(false);
        }

        // Create a new CampusMapController object and call the showMap method to display the updated map
        CampusMapController campusMapController = new CampusMapController();
        campusMapController.showMap();
    }


    /** This method is responsible for transitioning to a new view/window within the application.
     * @param file: The FXML file name for the target view.
     * @param title: The title for the new window.
     * @throws IOException
     */
    private void returnBack(String file, String title) throws IOException {
        // Declare and initialize the window size
        int v = 1080;
        int v1 = 800;

        // Create a new Stage object for the new window
        Stage stage = new Stage();

        // Create an FXMLLoader object and set the FXML file for the target view
        FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource(file));

        // Load the FXML file and create a Scene object with the specified window size
        Scene scene = new Scene(fxmlLoader.load(), v, v1);

        // Set the title for the new window
        stage.setTitle(title);

        // Set the scene for the new window
        stage.setScene(scene);

        // Set the new window to be non-resizable
        stage.setResizable(false);

        // Set the X and Y position of the new window
        stage.setX(200);
        stage.setY(70);
        // Display the new window
        stage.show();
    }


    @FXML
    private void initialize() {
        // Load and display the map in the editing mode
        loadMapForEditing();
        ObservableList<String> roomsToSelect = FXCollections.observableArrayList("Classrooms", "CS Labs","Collaborative Room", "Elevators", "Entry/Exit", "GenLabs", "Restaurant", "Stairwells" );
        roomSelector.setItems(roomsToSelect);
    }
    private void loadMapForEditing() {
        if (currentFloorMap != null) {
            try {
                URL mapUrl = getClass().getResource(currentFloorMap.getMapFileName());
                URI uri = mapUrl.toURI();
                InputStream stream = new FileInputStream(new File(uri));
                Image image = new Image(stream);
                double imageHeight = image.getHeight();
                double imageWidth = image.getWidth();

                ImageView imageView = new ImageView();
                imageView.setImage(image);
                imageView.setX(0);
                imageView.setY(0);
                imageView.setFitWidth(image.getWidth() * zoom);
                imageView.setPreserveRatio(true);

                Group root = new Group();
                root.getChildren().add(imageView);

                for (Layer layer : currentFloorMap.getLayers()) {
                    ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, layer);
                    root.getChildren().add(imageLayer);
                }

                mapPane.setContent(root);
            } catch (Exception ex) {
                // Handle the exception
                ex.printStackTrace();
            }
        }
    }

    /**
     * Handles the event when the "Close" button is clicked in the UI.
     *
     * @param actionEvent an ActionEvent object representing the click event
     * @throws RuntimeException if an IOException occurs during the execution of the returnBack method
     */
    public void onCloseButtonClick(ActionEvent actionEvent) throws RuntimeException {
        try {
            // Calls the returnBack method with the specified FXML file name and window title
            returnBack("main-view.fxml", "Western Campus Map");
        } catch (IOException ex) {
            // Throws a RuntimeException if an IOException occurs during the execution of returnBack
            throw new RuntimeException(ex);
        }

        // Hides the current window using the hide() method
        ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
    }


    public void onAddPOIButtonClick(ActionEvent actionEvent) {

    }
    public void onDeletePOIButtonClick(ActionEvent actionEvent) {

    }
    public void onEditPOIButtonClick(ActionEvent actionEvent) {

    }

    public FloorMap getCurrentFloorMap() {
        return currentFloorMap;
    }

    public void setCurrentFloorMap(FloorMap currentFloorMap) {
        this.currentFloorMap = currentFloorMap;
      //  loadMapForEditing();
    }
}

