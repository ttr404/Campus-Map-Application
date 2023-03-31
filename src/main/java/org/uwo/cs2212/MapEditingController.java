package org.uwo.cs2212;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.uwo.cs2212.model.*;

import java.io.*;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is the controller for the Campus Map application. It handles the UI actions,
 * such as zooming in and out, selecting points of interest (POIs), adding and deleting POIs,
 * and loading maps for different floors. It also contains methods for converting between
 * window and real map coordinate systems, as well as for handling mouse events.
 *
 * The CampusMapController class works in conjunction with the FloorMap, PointOfInterest,
 * ImageLayer, and Layer classes, as well as the FXML view files to provide a complete map
 * navigation and editing experience.
 *
 * @author Tingrui Zhang
 * @author Binchi Zhang
 *
 */
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
    private TextArea Description;
    @FXML
    private ComboBox<String> roomSelector;
    @FXML
    private ScrollPane scrollPane;
    private double zoom = 1.0;
    private double imageWidth;
    private double imageHeight;
    private List<PointOfInterest> pois = new ArrayList<>();
    private Circle currentSelectedPoiCircle;
    private PointOfInterest currentSelectedPoi;
    private FloorMap currentFloorMap;
    private Image currentFloorMap2;
    private double coordinateX;
    private double coordinateY;


    @FXML
    private void initialize() {
        ObservableList<String> roomsToSelect = FXCollections.observableArrayList("", "Accessibility","Washroom","Classroom", "CS Lab", "Collaborative Room", "Elevator", "Entry/Exit", "GenLab", "Restaurant", "Stairwell");
        roomSelector.setItems(roomsToSelect);
        //     scrollPane.setOnMouseClicked(this::onMapClicked);
        // pois = new ArrayList<>();

    }
//    protected void showPoiInfoPopup(PointOfInterest poi) {
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("POI Information");
//        alert.setHeaderText(poi.getName());
//        String content = "Room Number: " + poi.getRoomNumber() + "\n"
//                + "Room Type: " + poi.getType() + "\n"
//                + "Description: " + poi.getDescription();
//        alert.setContentText(content);
//        alert.showAndWait();
//    }

//    public void onMapClicked(MouseEvent event) {
//        Point2D windowPosition = new Point2D(event.getX(), event.getY());
//        Point2D realMousePosition = currentFloorMap.screenToLocal(windowPosition);
//        PointOfInterest clickedPoi = findClickedPoi(realMousePosition);
//
//        if (clickedPoi != null) {
//            showPoiInfo(clickedPoi); // Show the information dialog when a POI is clicked
//        }
//    }

    private void selectPoi(PointOfInterest selectedPoi) {
        if (currentSelectedPoi != null) {
            // Remove highlighting from the previously selected POI Circle
            if (currentSelectedPoiCircle != null) {
                currentSelectedPoiCircle.setStroke(Color.BLACK);
                currentSelectedPoiCircle.setStrokeWidth(1);
            }
        }
        currentSelectedPoi = selectedPoi;

        if (currentSelectedPoi != null) {
            // Highlight the newly selected POI Circle
            for (Node node : scrollPane.getChildrenUnmodifiable()) {
                if (node instanceof Circle) {
                    Circle circle = (Circle) node;
                    PointOfInterest poi = (PointOfInterest) circle.getUserData();

                    if (poi == currentSelectedPoi) {
                        currentSelectedPoiCircle = circle;
                        circle.setStroke(Color.YELLOW);
                        circle.setStrokeWidth(3);
                        break;
                    }
                }
            }
        } else {
            // Deselect the previously selected POI Circle if no POI is selected
            if (currentSelectedPoiCircle != null) {
                currentSelectedPoiCircle.setStroke(Color.BLACK);
                currentSelectedPoiCircle.setStrokeWidth(1);
                currentSelectedPoiCircle = null;
            }
        }
    }

    /**
     * This method is responsible for loading and displaying the map
     * of the currently selected floor. It takes the map file name from the
     * currentFloorMap, loads the image, and creates an ImageView to display it.
     * It also creates ImageLayer instances for each layer in the currentFloorMap,
     * adjusting their size and position based on the zoom factor.
     *
     * <p> In case of any exceptions during the loading or processing of the map
     * or layers, the exception is caught and no further action is taken. </p>
     */
    protected void showMap(){
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
            scrollPane.setContent(root);
        }
        catch(Exception ex)
        {}
    }

    protected PointOfInterest findClickedPoi(Point2D realMousePosition) {
        for (PointOfInterest poi : pois) {
            if (hitTest(realMousePosition, poi)) {
                return poi;
            }
        }
        return null;
    }

    private boolean hitTest(Point2D mousePosition, PointOfInterest poi){
        if (mousePosition.getX() <= poi.getX()+6/zoom && mousePosition.getX() >= poi.getX()-6/zoom && mousePosition.getY() <= poi.getY()+6/zoom && mousePosition.getY() >= poi.getY()-6/zoom){
            return true;
        }
        return false;
    }


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
        showMap();
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
        showMap();
    }

    /** This method is responsible for transitioning to a new view/window within the application.
     *
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

    /**
     * Loads the map for editing by retrieving the map file, creating an ImageView of the map,
     * and adding layers to the map as ImageLayers.
     *
     */
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
                scrollPane.setContent(root);
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

    public void onAddPOIButtonClick(ActionEvent actionEvent) throws IOException {
        boolean flag = false; // use flag for create new instance

        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName()));
        String json = reader.lines().collect(Collectors.joining());
        reader.close();

        JSONObject jsonObject = new JSONObject(json);

        if (!poiName.getText().isEmpty() && !roomNumber.getText().isEmpty() && roomSelector.getValue() != null){
            for (int i = 0; i < jsonObject.getJSONArray("layers").length(); i++) {

                if (jsonObject.getJSONArray("layers")
                        .getJSONObject(i)
                        .getJSONArray("points")
                        .getJSONObject(0)
                        .getString("type")
                        .toLowerCase()
                        .equals(roomSelector.getValue().toLowerCase())){
                    JSONObject point = new JSONObject()
                            .put("x", coordinateX)
                            .put("y", coordinateY)
                            .put("name", poiName.getText())
                            .put("roomNumber", roomNumber.getText())
                            .put("description", Description.getText())
                            .put("type", roomSelector.getValue());
                    jsonObject.getJSONArray("layers")
                            .getJSONObject(i)
                            .getJSONArray("points")
                            .put(point);
                    flag = true;
                    break;
                }
            }
        }

        FileWriter fileWriter = new FileWriter("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName());
        fileWriter.write(jsonObject.toString());

        fileWriter.close();

    }

    /**
     * Converts a point in the window coordinate system to a point in the real map coordinate system.
     * Takes into account the current zoom level and scroll position.
     *
     @param windowPoint The point in the window coordinate system
     @return The converted point in the real map coordinate system
     */
    private Point2D WindowPointToRealPoint(Point2D windowPoint){
        double windowXValue = (imageWidth - scrollPane.getViewportBounds().getWidth()/zoom) * scrollPane.getHvalue();
        double windowYValue = (imageHeight - scrollPane.getViewportBounds().getHeight()/zoom) * scrollPane.getVvalue();
        System.out.println("windowPosition:(" + windowXValue + ", " + windowYValue+")");
        double mouseX = windowXValue + windowPoint.getX()/zoom;
        double mouseY = windowYValue + windowPoint.getY()/zoom;
        if (scrollPane.getViewportBounds().getHeight() >= imageHeight){
            mouseY = windowPoint.getY()/zoom;
        }
        if (scrollPane.getViewportBounds().getWidth() >= imageWidth){
            mouseX = windowPoint.getX()/zoom;
        }
        System.out.println("mouse real position:(" + mouseX + ", " + mouseY+")");
        return new Point2D(mouseX, mouseY);
    }

    @FXML
    private void calculateRealMousePosition(MouseEvent mouseEvent){
        coordinateX = WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY())).getX();
        coordinateY = WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY())).getX();
    }




    public void onDeletePOIButtonClick(ActionEvent actionEvent) {

    }
    public void onEditPOIButtonClick(ActionEvent actionEvent) {

    }

    /**
     * Sets the current floor map and loads it for editing.
     *
     * @param currentFloorMap the FloorMap object representing the current floor map
     */
    public void setCurrentFloorMap(FloorMap currentFloorMap) {
        this.currentFloorMap = currentFloorMap;
        loadMapForEditing();
    }
}