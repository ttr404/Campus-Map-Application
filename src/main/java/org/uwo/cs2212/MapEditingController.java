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

import javafx.scene.shape.Circle;
import javafx.stage.*;
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
    private final List<PointOfInterest> pois = new ArrayList<>();
    private Circle currentSelectedPoiCircle;
    private PointOfInterest currentSelectedPoi;
    private FloorMap currentFloorMap;
    private Image currentFloorMap2;
    private double coordinateX;
    private double coordinateY;
    private String roomName = "";
    private String roomType = "";
    private String poiRoomNumber = "";
    private String roomDescription = "";
    private ContextMenu poiPopup;

    @FXML
    private void initialize() {
        ObservableList<String> roomsToSelect = FXCollections.observableArrayList("", "Accessibility","Washroom","Classroom", "CS Lab", "Collaborative Room", "Elevator", "Entry/Exit", "GenLab", "Restaurant", "Stairwell");
        roomSelector.setItems(roomsToSelect);

        // Disable the buttons initially
        editPOI.setDisable(true);
        deletePOI.setDisable(true);
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
     *
     * @return
     */
    protected void showMap(){
        try {
            URL mapUrl = CampusMapController.class.getResource(CurrentUser.getCurrentFloorMap().getMapFileName());
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
            Image coordinate = new Image(getClass().getResourceAsStream("map-marker.png"));
            ImageView coordinateView = new ImageView(coordinate);

            for(Layer layer: CurrentUser.getCurrentFloorMap().getLayers()){
                ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, layer);
                root.getChildren().add(imageLayer);

                for(PointOfInterest poi: layer.getPoints()){
                    if ((int) Math.round(poi.getX()) >= (coordinateX - 7)
                            && (int) Math.round(poi.getX()) <= (coordinateX + 7)
                            && (int) Math.round(poi.getY()) >= (coordinateY - 7)
                            && (int) Math.round(poi.getY()) <= (coordinateY + 7)) {
                        coordinateView.setVisible(false);
                    }
                }
            }

            // bug fixed
            if (coordinateX == 0 && coordinateY == 0){
                coordinateView.setVisible(false);
            }

            coordinateView.setLayoutX(coordinateX * zoom - 15);
            coordinateView.setLayoutY(coordinateY * zoom - 29);
            coordinateView.setFitWidth(30);
            coordinateView.setPreserveRatio(true);
            root.getChildren().add(coordinateView);

            scrollPane.setContent(root);
        }
        catch(Exception ex)
        {}
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

    /**
     * Checks whether a given point of interest (POI) is "hit" by a mouse click at the given real-world position, taking into account
     * the current zoom level of the map. If the distance between the POI and the mouse position is less than or equal to 6 pixels
     * (scaled according to the current zoom level), the POI is considered hit and this method returns true. Otherwise, it returns false.
     *
     @param mousePosition the real-world position of the mouse click
     @param poi the point of interest to check for a hit
     @return true if the POI is hit by the mouse click, false otherwise
     */
    private boolean hitTest(Point2D mousePosition, PointOfInterest poi){
        return mousePosition.getX() <= poi.getX() + 6 / zoom && mousePosition.getX() >= poi.getX() - 6 / zoom && mousePosition.getY() <= poi.getY() + 6 / zoom && mousePosition.getY() >= poi.getY() - 6 / zoom;
    }

    /**
     * Handles mouse clicks on the editor map by determining the real-world position of the click and checking if any point of interest
     * (POI) is located at that position. If a POI is found, its name, type, room number, and description are stored in the corresponding
     * fields, and the POI is selected. If no POI is found, the current POI is deselected. The map is then updated to show the selected
     * POI, if any.
     *
     * @param mouseEvent the MouseEvent object representing the mouse click event
     */
    @FXML
    private void onEditorMapClicked(MouseEvent mouseEvent) {
        if (poiPopup != null && poiPopup.isShowing()) {
            poiPopup.hide();
        }
        // Calculate the real mouse position
        Point2D realMousePosition = calculateRealMousePosition(mouseEvent);

            for (Layer layer : currentFloorMap.getLayers()) {
                for (PointOfInterest poi : layer.getPoints()) {
                    if (hitTest(realMousePosition, poi)) {
                        roomName = poi.getName();
                        roomType = poi.getType();
                        poiRoomNumber = poi.getRoomNumber();
                        roomDescription = poi.getDescription();
                        selectPoi(new SearchResult(currentFloorMap, poi));

                        /* Below: pop-up window wrote by @Truman, debugged and improved by @Tingrui */

                        // Create the ContextMenu
                        poiPopup = new ContextMenu();
                        poiPopup.setStyle("-fx-background-color: transparent;");
                        MenuItem menuItem = new MenuItem();
                        menuItem.setStyle("-fx-background-color: transparent; -fx-font-size: 12px;");

                        // Set the content for the ContextMenu
                        String s = "Name:" + "   " + poi.getName() + "\nType:    " + poi.getType() + "\nDescription:" + "  " + poi.getDescription();
                        menuItem = new MenuItem(s);
                        menuItem.setStyle("-fx-font-size: 12px;-fx-text-fill: black");
                        poiPopup.getItems().add(menuItem);

                        // Calculate the window position of the POI
                        Point2D poiRealPoint = new Point2D(poi.getX(), poi.getY());
                        Point2D poiWindowPoint = WindowPointToRealPoint(poiRealPoint);

                        // Show the context menu at the POI position
                        poiPopup.show(scrollPane.getScene().getWindow(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
                        return;
                    }
                }
        }
        selectPoi(new SearchResult(currentFloorMap, null));
    }


    /**
     * Selects a point of interest (POI) and updates the current floor map to the floor map associated with the POI's location.
     * If a POI is currently selected, its "selected" property is set to false before selecting the new POI. If a POI is selected,
     * its "selected" property is set to true. Finally, the map is updated to show the new floor map and the selected POI.
     *
     * @param searchResult the SearchResult object representing the selected POI and its associated floor map
     */
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

            if (currentSelectedPoi != null && currentSelectedPoi.isSelected()) {
                // Enable only edit and disable buttons
                addPOI.setDisable(true);
                editPOI.setDisable(false);
                deletePOI.setDisable(false);
            } else {
                // Enable only add button
                addPOI.setDisable(false);
                editPOI.setDisable(true);
                deletePOI.setDisable(true);
            }

            showMap();
        }
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
        if (poiPopup != null && poiPopup.isShowing()) {
            poiPopup.hide();
        }
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
        if (poiPopup != null && poiPopup.isShowing()) {
            poiPopup.hide();
        }
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
        int v1 = 830;

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
        Image icon = new Image(getClass().getResourceAsStream("western-logo.png"));
        stage.getIcons().add(icon);
        // Display the new window
        stage.show();
    }

    /**
     * Loads the map for editing by retrieving the map file, creating an ImageView of the map,
     * and adding layers to the map as ImageLayers.
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


    /**
     * Adds a new point of interest (POI) to the current floor map configuration file. The new POI's name, room number, description,
     * and type are obtained from the corresponding text fields and combo box in the GUI. If any of these fields are empty, an error
     * dialog is shown indicating that all fields must be filled. If a POI with the same name or room number already exists in the
     * configuration file, an error dialog is shown indicating that a different name or room number must be used. If a layer exists in
     * the configuration file with the same type as the new POI, the new POI is added to that layer's list of points. If no such layer
     * exists, a new layer is created and the new POI is added to that layer's list of points. If the POI is successfully added, an
     * information dialog is shown indicating success.
     *
     @param actionEvent the action event that triggered this method
     @throws IOException if an I/O error occurs while reading from or writing to the configuration file
     */
    public void onAddPOIButtonClick(ActionEvent actionEvent) throws IOException {
        boolean flag = false;

        if (coordinateX == 0 && coordinateY == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No coordinate selected");
            alert.setContentText("Please select a coordinate on the map");
            alert.showAndWait();
            return;
        }

        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName()));
        String json = reader.lines().collect(Collectors.joining());
        reader.close();

        JSONObject jsonObject = new JSONObject(json);

        if (!poiName.getText().isEmpty() && !roomNumber.getText().isEmpty() && roomSelector.getValue() != null){
            for (int i = 0; i < jsonObject.getJSONArray("layers").length(); i++) {
                for (int j = 0; j < jsonObject.getJSONArray("layers").getJSONObject(i).getJSONArray("points").length(); j++) {
                    JSONObject checkPOI = jsonObject.getJSONArray("layers")
                            .getJSONObject(i)
                            .getJSONArray("points")
                            .getJSONObject(j);
                    if (checkPOI.getString("name").equals(poiName.getText()) || checkPOI.getString("roomNumber").equals(roomNumber.getText())){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText("POI already exists");
                        alert.setContentText("Please enter a different POI name or room number");
                        alert.showAndWait();
                        return;
                    }
                }
                if (jsonObject.getJSONArray("layers")
                        .getJSONObject(i)
                        .getJSONArray("points")
                        .getJSONObject(0)
                        .getString("type")
                        .equalsIgnoreCase(roomSelector.getValue())){
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
            if (!flag){
                JSONArray jsonArray = new JSONArray();
                JSONObject layer = new JSONObject()
                        .put("name", "Main Map")
                        .put("color", "BLUE");
                if (roomSelector.getValue().equalsIgnoreCase("washroom") || roomSelector.getValue().equalsIgnoreCase("accessibility")){
                    layer.put("layerType", "base");
                }
                else {
                    layer.put("layerType", "internal");
                }
                layer.put("font", "Arial")
                        .put("size", 16)
                        .put("points", jsonArray);
                JSONObject point = new JSONObject()
                        .put("x", coordinateX)
                        .put("y", coordinateY)
                        .put("name", poiName.getText())
                        .put("roomNumber", roomNumber.getText())
                        .put("description", Description.getText())
                        .put("type", roomSelector.getValue());
                jsonArray.put(point);
                jsonObject.getJSONArray("layers").put(layer);
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("POI added");
            alert.setContentText("POI has been added to the map");
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("check_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
            return;
        }

        FileWriter fileWriter = new FileWriter("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName());
        fileWriter.write(jsonObject.toString());

        fileWriter.close();

    }
    private ImageLayer imageLayer;
    private double currentZoom;
    private Layer layer;


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

    /**
     * Calculates the real-world position of the mouse based on the given mouse event.
     *
     * @param mouseEvent the mouse event containing the mouse position in window coordinate.
     * @return a Point2D object representing the real-world position of the mouse
     */
    @FXML
    private Point2D calculateRealMousePosition(MouseEvent mouseEvent){
        coordinateX = WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY())).getX();
        coordinateY = WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY())).getY();
        showMap();
        return WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * Deletes a point of interest (POI) from the current floor map configuration file. The POI to be deleted is determined by
     * the values of the roomName, roomType, and poiRoomNumber fields, which must be set prior to calling this method. If the
     * POI is found and deleted, an information dialog is shown indicating success. If the POI is not found, an error dialog
     * is shown indicating that the POI could not be deleted.
     *
     @param actionEvent the action event that triggered this method
     @throws IOException if an I/O error occurs while reading from or writing to the configuration file
     */
    public void onDeletePOIButtonClick(ActionEvent actionEvent) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName()));
        String json = reader.lines().collect(Collectors.joining());
        reader.close();

        JSONObject jsonObject = new JSONObject(json);

        if (!roomName.isEmpty() && !roomType.isEmpty() && !poiRoomNumber.isEmpty()){
            outerloop:
            for (int i = 0; i < jsonObject.getJSONArray("layers").length(); i++){
                for (int j = 0; j < jsonObject.getJSONArray("layers").getJSONObject(i).getJSONArray("points").length(); j++) {
                    JSONObject checkPOI = jsonObject.getJSONArray("layers")
                            .getJSONObject(i)
                            .getJSONArray("points")
                            .getJSONObject(j);

                    if (checkPOI.getString("name").equalsIgnoreCase(roomName)
                            && checkPOI.getString("roomNumber").equalsIgnoreCase(poiRoomNumber)
                            && checkPOI.getString("type").equalsIgnoreCase(roomType)) {
                        jsonObject.getJSONArray("layers")
                                .getJSONObject(i)
                                .getJSONArray("points")
                                .remove(j);
                        break outerloop;
                    }
                }
            }
            roomName = "";
            roomType = "";
            poiRoomNumber = "";

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("POI deleted");
            alert.setContentText("POI has been deleted from the map");
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("check_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.setContentText("The POI you are trying to delete does not exist");
            alert.showAndWait();
            return;
        }

        FileWriter fileWriter = new FileWriter("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName());
        fileWriter.write(jsonObject.toString());

        fileWriter.close();

    }

    /**
     * Edits a point of interest (POI) in the current floor map configuration file. The POI to be edited is determined by the
     * values of the roomName, roomType, and poiRoomNumber fields, which must be set prior to calling this method. The new
     * values for the POI's name, room number, description, and type are obtained from the corresponding text fields and combo box
     * in the GUI. If any of these fields are empty, an error dialog is shown indicating that all fields must be filled. If the
     * POI is found and edited, an information dialog is shown indicating success. If the POI is not found, an error dialog
     * is shown indicating that a POI must be selected for editing.
     *
     @param actionEvent the action event that triggered this method
     @throws IOException if an I/O error occurs while reading from or writing to the configuration file
     */
    public void onEditPOIButtonClick(ActionEvent actionEvent) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName()));
        String json = reader.lines().collect(Collectors.joining());
        reader.close();

        JSONObject jsonObject = new JSONObject(json);

        if (!roomName.isEmpty() && !roomType.isEmpty() && !poiRoomNumber.isEmpty()){
            if (poiName.getText().isEmpty() || roomNumber.getText().isEmpty() || roomSelector.getValue() == null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error");
                alert.setContentText("Please enter at least one field");
                alert.showAndWait();
                return;
            }
            outerloop:
            for (int i = 0; i < jsonObject.getJSONArray("layers").length(); i++){
                for (int j = 0; j < jsonObject.getJSONArray("layers").getJSONObject(i).getJSONArray("points").length(); j++) {
                    JSONObject checkPOI = jsonObject.getJSONArray("layers")
                            .getJSONObject(i)
                            .getJSONArray("points")
                            .getJSONObject(j);

                    if (checkPOI.getString("name").equals(poiName.getText()) || checkPOI.getString("roomNumber").equals(roomNumber.getText())) {
                        if (checkPOI.getInt("x") <= (coordinateX - 7)
                                && checkPOI.getInt("x") >= (coordinateX + 7)
                                && checkPOI.getInt("y") <= (coordinateY - 7)
                                && checkPOI.getInt("y") >= (coordinateY + 7)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Error");
                            alert.setHeaderText("POI already exists");
                            alert.setContentText("Please enter a different POI name or room number");
                            alert.showAndWait();
                            return;
                        }
                    }

                    if (checkPOI.getString("name").equalsIgnoreCase(roomName)
                            && checkPOI.getString("roomNumber").equalsIgnoreCase(poiRoomNumber)
                            && checkPOI.getString("type").equalsIgnoreCase(roomType)) {
                        jsonObject.getJSONArray("layers")
                                .getJSONObject(i)
                                .getJSONArray("points")
                                .getJSONObject(j)
                                .put("x", coordinateX)
                                .put("y", coordinateY)
                                .put("name", poiName.getText())
                                .put("roomNumber", roomNumber.getText())
                                .put("description", Description.getText())
                                .put("type", roomSelector.getValue());
                        break outerloop;
                    }
                }
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
   //         alert.setTitle("Success");
            alert.setHeaderText("POI has been successfully edited");
     //       alert.setContentText("POI has been edited");
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("check_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error!");
   //         alert.setHeaderText("Error!");
            alert.setContentText("Please select a POI to edit");
            alert.showAndWait();
            return;
        }
        FileWriter fileWriter = new FileWriter("./src/main/resources/org/uwo/cs2212/" + currentFloorMap.getConfigFileName());
        fileWriter.write(jsonObject.toString());

        fileWriter.close();

    }
}