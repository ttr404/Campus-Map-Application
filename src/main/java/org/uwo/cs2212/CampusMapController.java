package org.uwo.cs2212;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;
import org.uwo.cs2212.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;

import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;


/**
 * The CampusMapController class is the main controller for managing the campus map UI and
 * its interactions. It provides functionality for displaying and interacting with floor maps,
 * points of interest (POIs), and map layers. The class also enables zooming in and out, as well
 * as adding, editing, and deleting POIs. It handles UI events such as button clicks and mouse events,
 * and manages the state of the currently selected floor map and POI.
 * -----
 * The CampusMapController is responsible for loading floor maps and associated POI data from
 * configuration files, managing the display of map elements such as POI circles, and providing
 * methods for converting between window and real map coordinate systems. The class uses the
 * FloorMap, PointOfInterest, ImageLayer, and SearchResult classes to  and manipulate
 * map-related data.
 *
 * @author Tingrui Zhang
 * @author Binchi Zhang
 * @author Truman Huang
 * @author Jarrett Boersen
 * @author Yaopeng Xie
 */
public class CampusMapController implements Initializable {

    public Button zoomReset;
    public Button clearIcon;
    public Button addPOI;
    public Button editPOI;
    public Button deletePOI;
    @FXML
    private Button floor0;
    @FXML
    private Button signOut;
    @FXML
    private Button floor1;
    @FXML
    private Button floor2;
    @FXML
    private Button floor3;
    @FXML
    private Button floor4;
    @FXML
    private Button favoriteButton;
    @FXML
    private Button showAllPOI;
    @FXML
    private Button zoomIn;
    @FXML
    private Button zoomOut;
    @FXML
    private Button help;
    @FXML
    private Button editButton;
    @FXML
    private Button about;
    @FXML
    private Button search;
    @FXML
    private Button clear;
    @FXML
    private ComboBox mapSelector;
    @FXML
    private TextField searchText;
    @FXML
    private ListView informationList;
    @FXML
    private ScrollPane mapPane;
    @FXML
    private CheckBox classrooms;
    @FXML
    private CheckBox stairwells;
    @FXML
    private CheckBox elevators;
    @FXML
    private CheckBox entryAndExit;
    @FXML
    private CheckBox genlabs;
    @FXML
    private CheckBox restaurants;
    @FXML
    private CheckBox cs_Labs;
    @FXML
    private CheckBox collaborative;
    @FXML
    private CheckBox user_POIs;

    private double zoom = 1.0;
    private double imageWidth;
    private double imageHeight;
    private Button currentSelectedFloorButton;
    private Button[] floorButtons;
    private double coordinateX = 0;
    private double coordinateY = 0;
    /**
     * Used to prevent the MapSelector from updating the map if favourite was pressed
     */
    boolean preventMapSelectorUpdatesFav = false;
    /**
     * Used to prevent the MapSelector from updating the map if search was pressed
     */
    boolean preventMapSelectorUpdatesSearch = false;

    /**
     * This method is called by the FXMLLoader when initialization is complete.
     * It initializes the map selector, search list view, displays all POI,
     * shows the floor buttons, shows the map, and sets the favourite button state.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMapSelector();
        initializeSearchListView();
        setShowAllPOI();
        showFloorButtons();
        showMap();

        // Disable the POI buttons
        setFavouriteButtonState();
        addPOI.setDisable(true);
        editPOI.setDisable(true);
        deletePOI.setDisable(true);

        floorButtons = new Button[]{floor0, floor1, floor2, floor3, floor4}; //initialize the floorButtons array
        editButton.setVisible(CurrentUser.isAdmin());
    }

    /**
     * This method initializes the map selector ComboBox by adding all available BaseMaps to the dropdown list.
     * It sets the currentBaseMap and currentFloorMap to the first available BaseMap and its first FloorMap respectively.
     * It also sets a listener to the value property of the map selector ComboBox to handle changes to the selected BaseMap.
     */
    private void initializeMapSelector() {
        for (BaseMap baseMap : CurrentUser.getMapConfig().getBaseMaps()) {
            mapSelector.getItems().add(baseMap.getName());
        }
        CurrentUser.setCurrentBaseMap(CurrentUser.getMapConfig().getBaseMaps().get(0));
        CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(0));

        mapSelector.valueProperty().setValue(CurrentUser.getCurrentBaseMap().getName());
        mapSelector.valueProperty().addListener((ov, oldValue, newValue) -> {
            // If preventMapSelectorUpdatesFav and preventMapSelectorUpdatesSearch are false then allow the
            // MapSelector to change the map
            if (!preventMapSelectorUpdatesFav && !preventMapSelectorUpdatesSearch) {
                handleComboBoxValueChanged(newValue);
            }
        });
    }

    /**
     * Initializes the search ListView by setting a listener for changes in the selected item and calling the
     * searchResultSelectionChanged method when a selection is made.
     */
    private void initializeSearchListView() { // need to change this to a different listview (Show all poi)
        MultipleSelectionModel<SearchResult> lvSelModel = informationList.getSelectionModel();
        lvSelModel.selectedItemProperty().addListener(
                (changed, oldVal, newVal) -> {
                    searchResultSelectionChanged(changed, oldVal, newVal);

                    // Hide the pin
                    clearPinIcon();

                    // Only find a BaseMap if newValue isn't null (it was run by the user)
                    if (newVal != null) {
                        BaseMap matchedBaseMap = BaseMap.findBaseMap(newVal.getFloorMap());

                        // If the BaseMap was found change the selector
                        if (matchedBaseMap != null) {
                            // Store the index of the matchedBaseMap
                            int index = 0;

                            // Determine the index of the current floor map by looping through all FloorMaps and
                            // increasing the index until the matching one is found
                            for (FloorMap floorMap : matchedBaseMap.getFloorMaps()) {
                                // Once the matching FloorMap is found stop
                                if (floorMap.equals(newVal.getFloorMap())) {
                                    break;
                                }
                                index++;
                            }

                            // Highlight the matching button for the current floor
                            highlightSelectedFloorButton(floorButtons[index]);

                            CurrentUser.setCurrentBaseMap(matchedBaseMap);
                            showFloorButtons();

                            // The setter for the selector must be called in this way when it's not
                            // called from the main thread
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    // Change the map selector's value to the current map
                                    mapSelector.valueProperty().setValue(matchedBaseMap.getName());
                                }
                            });
                        }
                    }
                });
    }

    public void onMapSelectorClicked() {
        // Allow the mapSelector to update
        preventMapSelectorUpdatesFav = false;
        preventMapSelectorUpdatesSearch = false;

        // Reset the cords to disable the Add button
        coordinateX = 0;
        coordinateY = 0;

        // Remove the unselected POI
        selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), null));
        showMap(); // TODO: Correct?
    }

    /**
     * Updates the checkboxes and the informationList based on which checkboxes are selected.
     * If a checkbox is selected, show the corresponding POI types on the map and add them to the informationList.
     * If a checkbox is not selected, hide the corresponding POI types on the map.
     * Also, clear the informationList and add the relevant POIs based on the selected checkboxes.
     */
    private void checkBoxSelected() {
        informationList.getItems().clear();

        // Show all POIs in the information list
        for (Layer layer : CurrentUser.getCurrentFloorMap().getLayers()) {
            for (PointOfInterest poi : layer.getPoints()) {
                if ((classrooms.isSelected() && poi.getType().equalsIgnoreCase("classroom"))
                        || (stairwells.isSelected() && poi.getType().equalsIgnoreCase("stairwell"))
                        || (elevators.isSelected() && poi.getType().equalsIgnoreCase("elevator"))
                        || (entryAndExit.isSelected() && poi.getType().trim().equalsIgnoreCase("entry/exit"))
                        || (genlabs.isSelected() && poi.getType().equalsIgnoreCase("genlab"))
                        || (restaurants.isSelected() && poi.getType().equalsIgnoreCase("restaurant"))
                        || (cs_Labs.isSelected() && poi.getType().equalsIgnoreCase("cs_labs"))
                        || (collaborative.isSelected() && poi.getType().equalsIgnoreCase("collaborative"))
                ) {
                    layer.setHideLayer(false);
                    informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));

                }
                if ((!classrooms.isSelected() && poi.getType().equalsIgnoreCase("classroom"))
                        || (!stairwells.isSelected() && poi.getType().equalsIgnoreCase("stairwell"))
                        || (!elevators.isSelected() && poi.getType().equalsIgnoreCase("elevator"))
                        || (!entryAndExit.isSelected() && poi.getType().trim().equalsIgnoreCase("entry/exit"))
                        || (!genlabs.isSelected() && poi.getType().equalsIgnoreCase("genlab"))
                        || (!restaurants.isSelected() && poi.getType().equalsIgnoreCase("restaurant"))
                        || (!cs_Labs.isSelected() && poi.getType().equalsIgnoreCase("cs_labs"))
                        || (!collaborative.isSelected() && poi.getType().equalsIgnoreCase("collaborative"))
                ) {
                    layer.setHideLayer(true);
                }
            }
        }

        // Get the user layer from the floor map
        UserLayer userLayer = CurrentUser.getCurrentFloorMap().getUserLayer();
        // Show the user POIs in the information list if there is data in the layer
        if (userLayer != null) {
            for (PointOfInterest poi : userLayer.getPoints()) {
                if (user_POIs.isSelected() && poi.getType().equalsIgnoreCase("user poi")) {
                    userLayer.setHideLayer(false);
                    informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));

                }
                if (!user_POIs.isSelected() && poi.getType().equalsIgnoreCase("user poi")) {
                    userLayer.setHideLayer(true);
                }
            }
        }

        showMap();
    }


    public void onClassrooms(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onStairwells(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onElevators(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onEntryAndExit(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onGenlabs(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onRestaurants(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onCS_Labs(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onCollaborative(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    public void onUserPOIs(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    /**
     * Shows or hides all points of interest based on the current filter settings. The method selects all
     * layers and checks the corresponding checkboxes, clears the informationList, and then adds all points
     * of interest in the current floor map to the informationList as SearchResult objects.
     */
    private void setShowAllPOI() {
        selectAllLayers();
        checkBoxSelected();
        for (Layer layer : CurrentUser.getCurrentFloorMap().getLayers()) {
            if (layer.getLayerType().equalsIgnoreCase("base")) {
                for (PointOfInterest poi : layer.getPoints()) {
                    informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
                }
            }
        }
    }

    @FXML
    private void onAllPOIButtonClicked(ActionEvent actionEvent) {
        setShowAllPOI();
    }

    private void searchResultSelectionChanged(ObservableValue<? extends SearchResult> changed, SearchResult oldVal, SearchResult newVal) {
        selectPoi(newVal);
    }

    /**
     * Handles the change event for the base map combo box. The method sets the currentBaseMap variable to the
     * BaseMap object selected by the user, and sets the currentFloorMap variable to the first floor map in the
     * currentBaseMap. The method then calls the showFloorButtons() and showMap() methods to display the floor
     * buttons and the map of the current floor, respectively. Finally, the method calls the setShowAllPOI() method
     * to show or hide all points of interest based on the current filter settings.
     *
     * @param newValue the new value of the base map combo box
     */
    private void handleComboBoxValueChanged(Object newValue) {
        for (BaseMap baseMap : CurrentUser.getMapConfig().getBaseMaps()) {
            if (newValue.toString().equals(baseMap.getName())) {
                CurrentUser.setCurrentBaseMap(baseMap);

                // Set the current floor map to the ground floor by default when switching maps
                CurrentUser.setCurrentFloorMap(baseMap.getFloorMaps().get(0));

                // Highlight the ground floor button by default when switching maps
                highlightSelectedFloorButton(floorButtons[0]);

                CurrentUser.setCurrentFloorMap(baseMap.getFloorMaps().get(0));
                showFloorButtons();
                showMap();
            }
        }
        coordinateX = 0;
        coordinateY = 0;
        setShowAllPOI();
    }

    /**
     * Displays the map of the current floor on the map pane. The method loads the image file for the current floor,
     * creates an ImageView object to display the image, and adds it to a new Group object. The method then creates
     * an ImageLayer object for each layer on the current floor, and adds it to the Group object. Finally, the Group
     * object is added to the map pane. If an error occurs while loading the image file or creating the ImageLayer
     * objects, the method catches the exception and returns.
     */
    protected void showMap() {
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

            // Show the POIs points on the map
            for (Layer layer : CurrentUser.getCurrentFloorMap().getLayers()) {
                ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, layer);
                root.getChildren().add(imageLayer);

                for (PointOfInterest poi : layer.getPoints()) {
                    if ((int) Math.round(poi.getX()) >= (coordinateX - 7)
                            && (int) Math.round(poi.getX()) <= (coordinateX + 7)
                            && (int) Math.round(poi.getY()) >= (coordinateY - 7)
                            && (int) Math.round(poi.getY()) <= (coordinateY + 7)) {
                        coordinateView.setVisible(false);
                    }
                }
            }

            // Get the user layer from the floor map
            UserLayer userLayer = CurrentUser.getCurrentFloorMap().getUserLayer();
            // Show the user POIs if the checkbox is selected
            if (user_POIs.isSelected() && userLayer != null) {
                ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, userLayer);
                root.getChildren().add(imageLayer);
                for (PointOfInterest poi : userLayer.getPoints()) {
                    if ((int) Math.round(poi.getX()) >= (coordinateX - 7)
                            && (int) Math.round(poi.getX()) <= (coordinateX + 7)
                            && (int) Math.round(poi.getY()) >= (coordinateY - 7)
                            && (int) Math.round(poi.getY()) <= (coordinateY + 7)) {
                        coordinateView.setVisible(false);
                    }
                }
            }

            if (coordinateX == 0 && coordinateY == 0) {
                coordinateView.setVisible(false);
            }

            coordinateView.setLayoutX(coordinateX * zoom - 15);
            coordinateView.setLayoutY(coordinateY * zoom - 29);
            coordinateView.setFitWidth(30);
            coordinateView.setPreserveRatio(true);
            root.getChildren().add(coordinateView);

            mapPane.setContent(root);
        } catch (Exception ex) {
            // Handle the exception
            ex.printStackTrace();
        }
    }

    /**
     * Handles the click event for the about button by displaying an about text to the user. The method creates
     * a new stage and sets up a Label control to display information about the app and its creators. The method
     * then displays the dialog and returns.
     *
     * @param event the ActionEvent object representing the about button click event
     */
    @FXML
    private void aboutButtonAction(ActionEvent event) {
        Stage stage = new Stage();
        String s = "Western Campus Navigation App\n" +
                "Version: 1.3.4\n" +
                "Release Date: April 5, 2023\n" +
                "\n" +
                "Our Team:\n" +
                "\n" +
                "\t1) Boersen, Jarrett (Student): jboerse2@uwo.ca\n" +
                "\t2) Huang, Truman (Student): yhuan939@uwo.ca\n" +
                "\t3) Xie, Yaopeng (Student): yxie447@uwo.ca\n" +
                "\t4) Zhang, Binchi (Student): bzhan484@uwo.ca\n" +
                "\t5) Zhang, Tingrui (Student): tzhan425@uwo.ca\n\n" +

                "Contact Us:\n\n" +
                "If you have any questions, feedback or suggestions, please feel free to reach out to us at bzhan484@uwo.com. We are always happy to hear from our users and help you in any way we can.\n" +
                "\n" +
                "Thank you for using our Western Campus Navigation App!";

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());

        // Create a Label with wrapped text and a fixed width
        Label aboutLabel = new Label(s);
        aboutLabel.setWrapText(true);

        VBox vbox = new VBox(aboutLabel);
        vbox.setPadding(new Insets(10));

        Scene scene = new Scene(vbox, 300, 100);
        stage.setScene(scene);
//        stage.setX(((Node) event.getSource()).getScene().getWindow().getX() + ((Node) event.getSource()).getScene().getWindow().getWidth() - 600);
//        stage.setY(((Node) event.getSource()).getScene().getWindow().getY());
        stage.centerOnScreen();
        stage.setMinWidth(310);
        stage.setMinHeight(430);
        stage.setMaxWidth(310);
        stage.setMaxHeight(430);

        Image icon = new Image(getClass().getResourceAsStream("western-logo.png"));
        stage.getIcons().add(icon);
        stage.setTitle("About");

        stage.show();
    }

    /**
     * Handles the click event for the help button by displaying a help dialog to the user. The method creates a
     * new stage and sets up a ChoiceBox control for selecting the help topic, and a Label control for displaying
     * the help text. The method sets the initial help text based on the default value of the ChoiceBox control,
     * and adds an event listener to the control to update the help text based on the selected value. The method
     * then displays the dialog and returns.
     *
     * @param event the ActionEvent object representing the help button click event
     * @throws IOException if the FXML file for the help dialog cannot be loaded
     */
    @FXML
    private void helpButtonAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) event.getSource()).getScene().getWindow());

        // Create a ChoiceBox control to select the help topic
        ChoiceBox<String> helpTopic = new ChoiceBox<>();
        helpTopic.getItems().addAll("Getting Started", "Search Function", "POI and Favorite", "Editor Mode");
        helpTopic.setValue("Getting Started"); // Set the default value
        Label helpLabel = new Label();
        helpLabel.setWrapText(true); // Wrap text to multiple lines

        VBox vbox = new VBox(new Label(""), helpTopic, helpLabel);
        vbox.setPadding(new Insets(10));

        // Wrap the VBox in a ScrollPane
        ScrollPane scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToWidth(true);

        Scene scene = new Scene(scrollPane);

        stage.setScene(scene);
        stage.setWidth(600);
        stage.setHeight(550);
        stage.setMinWidth(300);
        stage.setMinHeight(500);
//        stage.setX(((Node) event.getSource()).getScene().getWindow().getX() + ((Node) event.getSource()).getScene().getWindow().getWidth() - stage.getWidth());
//        stage.setY(((Node) event.getSource()).getScene().getWindow().getY());
        stage.centerOnScreen();

        Image icon = new Image(getClass().getResourceAsStream("western-logo.png"));
        stage.getIcons().add(icon);
        stage.setTitle("Help");

        stage.show();

        // Set the initial help text
        String helpText = getHelpText(helpTopic.getValue());
        helpLabel.setText(helpText); // Set the text of the helpLabel control

        // Add an event listener to the helpTopic control to update the help text
        helpTopic.setOnAction(e -> {
            String selectedTopic = helpTopic.getValue();
            String selectedHelpText = getHelpText(selectedTopic);
            helpLabel.setText(selectedHelpText);
        });
    }

    // Helper method to get the help text for a given topic
    private String getHelpText(String topic) {
        switch (topic) {
            case "Getting Started":
                return """

                        To view the map, simply open the app, login with your credentials and the campus map will be displayed. To search for a specific location, click on the search bar at the top of the screen and type in the name of the location you are looking for then click the “Search” button. The map will display the location and provide some related information.

                        To zoom in and out of the map, use the “+” and “-” zoom buttons located near the top left of the screen. Use the “Reset” button to return the default zoom size.

                        To change the map view to a specific building, click on the choice box located under the “Sign Out” button.

                        You can select from a variety of buildings, including Middlesex College, Western Science Centre and the Physics and Astronomy Building.

                        For each building, you can select different floors using the floor buttons located at the top middle of the screen.""";
            case "POI and Favorite":
                return """

                        To use the favourite and POI (point of interest) functions in the map navigation app, you can do the following:
                                                
                        1. To add a POI to the map, click on the desired point on the map and then click the "Add POI" button located at the top of the screen.
                                                
                        2. Fill in the name and any relevant details about the POI, such as a room number and description. You can also add the new POI to your favorites at the same time.
                                                
                        3. Click "Save" to add the POI to your map.
                                                
                        4. To add a location to your favourites, click on a POI point on the map or a search result from the list.
                                                
                        5. Once you have selected a location, click the Favorite button, it's the one with a star, to add it to your favourites list. This button should be highlighted or easily visible when clicking on the appropriate POI.
                                                
                        6. To access your favourites, click the "Favorites" button in the app. This will display a list of all the locations you have saved as favourites.
                                                
                        7. To remove a POI or favourite from your list, click on the item and then click the "Favorite" button.
                                                
                        By using the favourite and POI functions in a map navigation app, you can quickly and easily save important locations and points of interest for future reference. This can make it easier to navigate to these locations in the future.""";


            case "Editor Mode":
                return """

                        To use the Editor mode in a map navigation app, follow these steps:
                                                
                        1. Log in to your admin account.
                                                
                        2. Click on the "Editor" mode button in the app.
                                                
                        3. Once in Editor mode, you can add, delete, or edit POIs on the map.
                                                
                        4. To add a new POI, select a location on the map and click the "Add POI" button at the top of the screen.
                                                
                        5. Fill in the required details, including the POI name, room number, and room type, by selecting a choice from the drop-down box.
                                                
                        6. To delete or edit an existing POI, select the POI on the map and click either the "Delete" or "Edit" buttons.
                                                
                        7. If you choose to edit the POI, make any necessary changes to the details and then click "Edit" to save the changes
                                                
                        8. Click "Close" to save the change of POI and return to the main page.
                                              
                        Using the Editor mode allows admin users to customize the POIs on the map and keep them up-to-date with the latest information, which can improve the accuracy and usefulness of the app for all users.""";
            case "Search Function":
                return """

                        To use the search function in a map navigation app, follow these steps:

                        1. Type in your desired destination or point of interest and click search. For example, you could search for a specific classroom, lab name, or type of place (e.g., "lab101").

                        2. The app will display a list of results that match your search terms. Select the result you want to navigate to in the search list below.

                        3. If you want to quickly find a specific type of point of interest, look for the checkboxes on the map that correspond to different categories, such as washroom or classroom.

                        4. Finally, if you have any favorite locations saved in the app, you can access them by tapping on the "Favorites" button. This will show you a list of all the locations you have saved, so you can easily navigate to them without having to search for them again.""";
            default:
                return "";
        }
    }

    /**
     * Signs the user out and returns them to the login page. This method changes the view to the login page and hides the
     * current window.
     *
     * @param actionEvent the ActionEvent object representing the event that triggered the sign-out action
     * @throws IOException if an error occurs while loading the login page view
     */
    @FXML
    private void signOut(ActionEvent actionEvent) throws IOException {
        returnBack("login-view.fxml", "Login Page", actionEvent);
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    /**
     * Returns the user to the previous view by loading the FXML file and creating a new stage for it. The method sets
     * the stage for the LoginViewController and creates a new scene with the specified FXML file and dimensions. If
     * the FXML file is for the login view, the method also sets up a key event handler to handle the Enter key and
     * log the user in. The method returns an FXMLLoader object for the loaded FXML file.
     *
     * @param file        the name of the FXML file to be loaded
     * @param title       the title of the new stage
     * @param actionEvent an ActionEvent object representing the click event
     * @return an FXMLLoader object for the loaded FXML file
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private FXMLLoader returnBack(String file, String title, ActionEvent actionEvent) throws IOException {
        int v = 1080;
        int v1 = 830;

        // Make the window not full screen in macOS to prevent a crash
        if (System.getProperty("os.name").toLowerCase().contains("mac")) {
            Node source = (Node) actionEvent.getSource();
            Window theStage = source.getScene().getWindow();
            ((Stage) theStage).setFullScreen(false);
        }

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource(file));
        LoginViewController.setStage(stage);

        if (file.compareTo("login-view.fxml") == 0) {
            v = 571;
            v1 = 400;
        }

        Scene scene = new Scene(fxmlLoader.load(), v, v1);

        if (file.compareTo("login-view.fxml") == 0) {
            LoginViewController controller = fxmlLoader.getController();
            scene.setOnKeyPressed(new EventHandler<>() {
                @Override
                public void handle(KeyEvent keyEvent) {
                    if (keyEvent.getCode().toString().equals("ENTER")) {
                        try {
                            controller.logIn();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            });
        }

        stage.setTitle(title);
        stage.setScene(scene);
        if (file.compareTo("login-view.fxml") == 0) {
            stage.setResizable(false);
        } else {
            stage.setResizable(true);
            stage.setMinHeight(410);
            stage.setMinWidth(600);
        }
//        stage.setX(200);
//        stage.setY(70);
        Image icon = new Image(getClass().getResourceAsStream("western-logo.png"));
        stage.getIcons().add(icon);
        stage.show();

        return fxmlLoader;
    }


    @FXML
    private void onZoomInButtonClicked(ActionEvent actionEvent) {
        if (zoom > 0.5) {
            zoom *= 0.8;
        } else {
            zoomIn.setDisable(false);
        }
        showMap();
    }

    @FXML
    private void onZoomOutButtonClicked(ActionEvent actionEvent) {
        if (zoom < 1.7) {
            zoom *= 1.2;
        } else {
            zoomOut.setDisable(false);
        }
        showMap();
    }

    @FXML
    private void onZoomResetButtonClicked(ActionEvent actionEvent) {
        zoom = 1;
        zoomReset.setDisable(false);
        showMap();
    }

    public void onClearIconButtonClicked(ActionEvent actionEvent) {
        // Remove the unselected POI
        selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), null));

        // Clear the pin
        clearPinIcon();
    }

    /**
     * This method is used to clear the pin icon from the map
     */
    public void clearPinIcon() {
        coordinateX = 0;
        coordinateY = 0;
        showMap();
    }

    /**
     * Highlights the selected floor button and clears the highlight from the previously selected floor button. This method
     * sets the background color and text color for the selected button and removes the styling from the previous one.
     *
     * @param selectedButton the Button object representing the newly selected floor button
     */
    private void highlightSelectedFloorButton(Button selectedButton) {
        // Clear the style for the previously selected floor button
        if (currentSelectedFloorButton != null) {
            currentSelectedFloorButton.setStyle("");
        }

        // Set the style for the newly selected floor button
        selectedButton.setStyle("-fx-background-color: #b3dee5; -fx-text-fill: white;");

        // Update the current selected floor button
        currentSelectedFloorButton = selectedButton;
    }


    @FXML
    private void onFloorGButtonClick(ActionEvent actionEvent) {
        CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(0));
        coordinateX = 0;
        coordinateY = 0;
        showMap();

        Button clickedButton = (Button) actionEvent.getSource();
        highlightSelectedFloorButton(clickedButton);
    }

    @FXML
    private void onFloor1ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 2) {
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(1));
            coordinateX = 0;
            coordinateY = 0;
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    @FXML
    private void onFloor2ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 3) {
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(2));
            coordinateX = 0;
            coordinateY = 0;
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    @FXML
    private void onFloor3ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 4) {
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(3));
            coordinateX = 0;
            coordinateY = 0;
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    @FXML
    private void onFloor4ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 5) {
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(4));
            coordinateX = 0;
            coordinateY = 0;
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    private void showFloorButtons() {
        int floorCount = CurrentUser.getCurrentBaseMap().getFloorMaps().size();
        if (floorCount <= 1) {
            floor0.setVisible(false);
            floor1.setVisible(false);
            floor2.setVisible(false);
            floor3.setVisible(false);
            floor4.setVisible(false);
        } else if (floorCount == 2) {
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(false);
            floor3.setVisible(false);
            floor4.setVisible(false);
        } else if (floorCount == 3) {
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(true);
            floor3.setVisible(false);
            floor4.setVisible(false);
        } else if (floorCount == 4) {
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(true);
            floor3.setVisible(true);
            floor4.setVisible(false);
        } else {
            floor0.setVisible(true);
            floor1.setVisible(true);
            floor2.setVisible(true);
            floor3.setVisible(true);
            floor4.setVisible(true);
        }
    }

    /**
     * Handles a mouse click event on the map. This method calculates the real-world mouse position based on the
     * mouse event, and then iterates through all points of interest on the current floor map to determine if
     * the mouse click hits a point of interest. If a point of interest is hit, the method selects the
     * point of interest, shows it in the informationList ListView, and returns. If no point of interest is hit,
     * the method deselects any currently selected POI and returns. It is also used to set that the pop-up window
     * should show when the Add POI button is pressed by setting mapClicked to true
     *
     * @param mouseEvent the MouseEvent object representing the mouse click event
     */
    public void onMapClicked(MouseEvent mouseEvent) {
        Point2D realMousePosition = calculateRealMousePosition(mouseEvent);
        for (Layer layer : CurrentUser.getCurrentFloorMap().getLayers()) {
            for (PointOfInterest poi : layer.getPoints()) {
                if (hitTest(realMousePosition, poi)) {
                    selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
                    showPoiInList(poi);

                    poiDetailsPopup(mouseEvent, poi);

                    return;
                }
            }
        }

        // User created POIs
        if (CurrentUser.getCurrentFloorMap().getUserLayer() != null && user_POIs.isSelected()) {
            for (PointOfInterest poi : CurrentUser.getCurrentFloorMap().getUserLayer().getPoints()) {
                if (hitTest(realMousePosition, poi)) {
                    selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
                    showPoiInList(poi);
                    if ((int) Math.round(poi.getX()) >= (coordinateX - 7)
                            && (int) Math.round(poi.getX()) <= (coordinateX + 7)
                            && (int) Math.round(poi.getY()) >= (coordinateY - 7)
                            && (int) Math.round(poi.getY()) <= (coordinateY + 7)) {
                        coordinateX = 0;
                        coordinateY = 0;
                    }

                    poiDetailsPopup(mouseEvent, poi);
                    //showMap(); // TODO: Keep removed?

                    return;
                }
            }
        }

        // Remove the unselected POI and refresh the map
        selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), null));
        showMap();
    }

    /**
     * Displays a popup window with details about the given PointOfInterest object.
     *
     * @param mouseEvent the mouse event that triggered the popup
     * @param poi        the PointOfInterest to display details for
     */
    private void poiDetailsPopup(MouseEvent mouseEvent, PointOfInterest poi) {
        /* Below: pop-up window wrote by @Truman, debugged and improved by @Tingrui */

        // Create the ContextMenu
        ContextMenu poiPopup = new ContextMenu();
        poiPopup.setStyle("-fx-background-color: transparent;");
        MenuItem menuItem = new MenuItem();
        menuItem.setStyle("-fx-background-color: transparent; -fx-font-size: 12px;");

        // Set the content for the ContextMenu
        String s = "Name:" + "   " + poi.getName() + "\nType:    " + poi.getType() +
                "\nDescription:" + "  " + poi.getDescription();
        menuItem = new MenuItem(s);
        menuItem.setStyle("-fx-font-size: 12px;-fx-text-fill: black");
        poiPopup.getItems().add(menuItem);

        // Calculate the window position of the POI
        // Point2D poiRealPoint = new Point2D(poi.getX(), poi.getY());
        // Point2D poiWindowPoint = WindowPointToRealPoint(poiRealPoint);

        // Show the context menu at the POI position
        poiPopup.show(mapPane.getScene().getWindow(), mouseEvent.getScreenX(), mouseEvent.getScreenY());
    }

    /**
     * Selects a Point of Interest (POI) based on the given SearchResult object. The method sets the currentFloorMap
     * to the floor map associated with the SearchResult object. It also updates the selected state of the
     * currentSelectedPoi and the newly selected POI. If the newly selected POI is not within the viewport,
     * this method centralizes the POI on the map. It then shows the map, sets the favourite button state, and returns.
     *
     * @param searchResult the SearchResult object containing the POI to be selected
     * @throws NullPointerException if the SearchResult object is null
     */
    private void selectPoi(SearchResult searchResult) {
        if (searchResult != null) {
            CurrentUser.setCurrentFloorMap(searchResult.getFloorMap());

            // Reset the selection status of the currently selected POI if one had been selected previously
            if (CurrentUser.getCurrentSelectedPoi() != null) {
                CurrentUser.getCurrentSelectedPoi().setSelected(false);
            }

            // Set the currently selected POI to the one from the search result
            CurrentUser.setCurrentSelectedPoi(searchResult.getPoi());

            // If there is a currently selected POI set it as selected as long as there is also a POI in the search
            // result
            if (CurrentUser.getCurrentSelectedPoi() != null) {
                CurrentUser.getCurrentSelectedPoi().setSelected(true);
            }

            // Only centralize the selected POI and update the map if a POI has actually been selected
            if (CurrentUser.getCurrentSelectedPoi() != null) {
                centralizeSelectedPoi();
                showMap();
            }

            // Allow the user to interact with the POI buttons or not
            setFavouriteButtonState();
            // If the currently selected POI is not null, and it is of type user POI then set
            // the editor buttons to edit and delete
            if (CurrentUser.getCurrentSelectedPoi() != null
                    && CurrentUser.getCurrentSelectedPoi().getType().equalsIgnoreCase("user poi")) {
                addPOI.setDisable(true);
                editPOI.setDisable(false);
                deletePOI.setDisable(false);
                // Otherwise, the currently selected POI is not null, and it is not of type
                // user POI then set the editor buttons to disabled
            } else if (searchResult.getPoi() != null && CurrentUser.getCurrentSelectedPoi() != null
                    && !CurrentUser.getCurrentSelectedPoi().getType().equalsIgnoreCase("user poi")) {
                addPOI.setDisable(true);
                editPOI.setDisable(true);
                deletePOI.setDisable(true);
                // Otherwise, if the coordinates are off the map disable the buttons
            } else if (coordinateX <= 0 && coordinateY <= 0) {
                addPOI.setDisable(true);
                editPOI.setDisable(true);
                deletePOI.setDisable(true);
            } else { // Otherwise, show the add button since the user clicked on the map
                addPOI.setDisable(false);
                editPOI.setDisable(true);
                deletePOI.setDisable(true);
            }
        }
    }

    /**
     * Shows the given PointOfInterest in the informationList ListView.
     * Clears the previous items in the list and adds a new SearchResult object containing the given poi.
     *
     * @param poi the PointOfInterest to show in the list
     */
    private void showPoiInList(PointOfInterest poi) {
        informationList.getItems().clear();
        informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
    }

    /**
     * Determines if the given mouse position hits the given point of interest.
     *
     * @param mousePosition The mouse position as a Point2D object.
     * @param poi           The point of interest as a PointOfInterest object.
     * @return True if the mouse position hits the point of interest, false otherwise.
     */
    private boolean hitTest(Point2D mousePosition, PointOfInterest poi) {
        return mousePosition.getX() <= poi.getX() + 6
                / zoom && mousePosition.getX() >= poi.getX() - 6
                / zoom && mousePosition.getY() <= poi.getY() + 6
                / zoom && mousePosition.getY() >= poi.getY() - 6 / zoom;
    }

    /**
     * Converts a point in the window coordinate system to a point in the real map coordinate system.
     * Takes into account the current zoom level and scroll position.
     *
     * @param windowPoint The point in the window coordinate system
     * @return The converted point in the real map coordinate system
     */
    private Point2D WindowPointToRealPoint(Point2D windowPoint) {
        double windowXValue = (imageWidth - mapPane.getViewportBounds().getWidth() / zoom) * mapPane.getHvalue();
        double windowYValue = (imageHeight - mapPane.getViewportBounds().getHeight() / zoom) * mapPane.getVvalue();
        System.out.println("windowPosition:(" + windowXValue + ", " + windowYValue + ")");
        double mouseX = windowXValue + windowPoint.getX() / zoom;
        double mouseY = windowYValue + windowPoint.getY() / zoom;
        if (mapPane.getViewportBounds().getHeight() >= imageHeight) {
            mouseY = windowPoint.getY() / zoom;
        }
        if (mapPane.getViewportBounds().getWidth() >= imageWidth) {
            mouseX = windowPoint.getX() / zoom;
        }
        System.out.println("mouse real position:(" + mouseX + ", " + mouseY + ")");
        return new Point2D(mouseX, mouseY);
    }

    /**
     * Calculates the real mouse position based on the window coordinates of the mouse event.
     * Updates the class variables 'coordinateX' and 'coordinateY' with the real X and Y values.
     * Calls the 'showMap' method to redraw the map.
     *
     * @param mouseEvent the MouseEvent object containing the window coordinates of the mouse
     * @return the Point2D object representing the real coordinates of the mouse
     */
    private Point2D calculateRealMousePosition(MouseEvent mouseEvent) {
        coordinateX = WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY())).getX();
        coordinateY = WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY())).getY();
        showMap();
        return WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * This method is called when the search button is clicked. It searches for the text entered in the search
     * text field in all the layers of the current base map and adds the search results to the informationList
     * ListView. If the text field is empty, no search results are displayed.
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    public void onSearchButtonClicked(ActionEvent actionEvent) {
        // Don't allow the mapSelector to update because search was pressed
        preventMapSelectorUpdatesFav = false;
        preventMapSelectorUpdatesSearch = true;

        selectAllLayers();
        checkBoxSelected();

        String text = searchText.getText().toLowerCase().trim();
        if (!text.equals("")) {
            informationList.getItems().clear();
            for (BaseMap baseMap : CurrentUser.getMapConfig().getBaseMaps()) {
                for (FloorMap floorMap : baseMap.getFloorMaps()) {
                    for (Layer layer : floorMap.getLayers()) {
                        for (PointOfInterest poi : layer.getPoints()) {
                            if (contains(poi.getName(), text) || contains(poi.getRoomNumber(), text) ||
                                    contains(poi.getType(), text) || contains(poi.getDescription(), text)) {
                                informationList.getItems().add(new SearchResult(floorMap, poi));
                            }
                        }
                    }
                    // Make sure the list of UserLayers isn't empty
                    if (floorMap.getUserLayer() != null) {
                        // Loop through the user-created POIs and check if they are a favourite
                        for (PointOfInterest poi : floorMap.getUserLayer().getPoints()) {
                            // Check if the user-created POI is in the layer
                            if (contains(poi.getName(), text) || contains(poi.getRoomNumber(), text) ||
                                    contains(poi.getType(), text) || contains(poi.getDescription(), text)) {
                                // Add the user-created POI to the informationList as a SearchResult
                                informationList.getItems().add(new SearchResult(floorMap, poi));
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * Checks if a string contains a target string, ignoring case.
     *
     * @param target the target string to search for
     * @param text   the text string to search in
     * @return true if the target string is found in the text string, false otherwise
     */
    private static boolean contains(String target, String text) {
        return target != null && target.toLowerCase().contains(text);
    }

    /**
     * Sets the state of the favorite button based on the current selected point of interest.
     * If there is no selected point of interest, the favorite button is disabled and set to
     * the default state (not a favorite).
     * If there is a selected point of interest, the favorite button is enabled and set to the
     * appropriate state (favorite or not favorite) based on the selected point of interest's favorite status.
     */
    private void setFavouriteButtonState() {
        ImageView imageView;
        // Only enable the favorite button if the currently selected POI is not null and is actually selected
        if (CurrentUser.getCurrentSelectedPoi() != null && CurrentUser.getCurrentSelectedPoi().isSelected()) {
            favoriteButton.setDisable(false);
            if (CurrentUser.getCurrentSelectedPoi().isFavorite()) {
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


    /**
     * Handles the event when the favorite button is clicked. Toggles the favorite state of the
     * currently selected POI and updates the favorite button state accordingly. If the currently
     * selected POI is a user-defined POI, also removes it from the user's favorites list in the user data.
     *
     * @param actionEvent the event triggered by clicking the favorite button
     */
    public void onFavoriteButtonClicked(ActionEvent actionEvent) {
        selectAllLayers();

        PointOfInterest currentPOI = CurrentUser.getCurrentSelectedPoi();

        if (currentPOI != null) {
            currentPOI.setFavorite(!currentPOI.isFavorite());

            // Set the POI as a favourite or not for the user
            FavoritePoi.setUserFavourite(currentPOI);

            setFavouriteButtonState();
        }
    }

    /**
     * Clears the current selection by deselecting all layers, unchecking all checkboxes and clearing the search text.
     *
     * @param actionEvent the event that triggered the button click
     */
    public void onClearButtonClicked(ActionEvent actionEvent) {
        deselectAllLayers();
        checkBoxSelected();
        searchText.setText("");
    }

    /**
     * Populates the informationList with favorite Points of Interest (POIs) when the "List Favorites" button is clicked.
     * ------
     * This method clears the current content of the informationList and iterates through all the BaseMaps, FloorMaps, Layers,
     * and UserLayers to find favorite POIs. If a POI is marked as a favorite, it is added to the informationList as a SearchResult.
     *
     * @param actionEvent the event object representing the button click
     */
    public void onListFavoritesButtonClicked(ActionEvent actionEvent) {
        // Don't allow the mapSelector to update because favorite list was pressed
        preventMapSelectorUpdatesFav = true;
        preventMapSelectorUpdatesSearch = false;

        // Show all the layers show the user can see them
        selectAllLayers();
        checkBoxSelected();

        // Clear the current content of the informationList
        informationList.getItems().clear();

        // Iterate through all BaseMaps
        for (BaseMap baseMap : CurrentUser.getMapConfig().getBaseMaps()) {
            // Iterate through all FloorMaps in each BaseMap
            for (FloorMap floorMap : baseMap.getFloorMaps()) {
                // Iterate through all Layers in each FloorMap
                for (Layer layer : floorMap.getLayers()) {
                    // Iterate through all Points of Interest (POIs) in each Layer
                    for (PointOfInterest poi : layer.getPoints()) {
                        // Check if the POI is marked as a favorite
                        if (poi.isFavorite()) {
                            // Add the favorite POI to the informationList as a SearchResult
                            informationList.getItems().add(new SearchResult(floorMap, poi));
                        }
                    }
                }

                // Make sure the list of UserLayers isn't empty
                if (floorMap.getUserLayer() != null) {
                    // Loop through the user-created POIs and check if they are a favourite
                    for (PointOfInterest poi : floorMap.getUserLayer().getPoints()) {
                        // Check if the user-created POI is marked as a favorite
                        if (poi.isFavorite()) {
                            // Add the favorite user-created POI to the informationList as a SearchResult
                            informationList.getItems().add(new SearchResult(floorMap, poi));
                        }
                    }
                }
            }
        }
    }

    /**
     * Centralizes the currently selected Point of Interest (POI) on the map by scrolling
     * the view to show the POI in the center of the screen.
     * If the currentSelectedPoi is null, this method does nothing.
     * If the currentSelectedPoi is not null and lies within the viewport, this method
     * returns without doing anything.
     * ------
     * If the currentSelectedPoi is not null and lies outside the viewport,
     * this method calculates the desired scroll position for both the horizontal
     * and vertical scroll bars based on the currentSelectedPoi's position and the
     * dimensions of the viewport and the map image. If the currentSelectedPoi is
     * closer to the left edge of the viewport, the method sets the horizontal scroll bar to 0;
     * if it is closer to the right edge, the method sets the horizontal scroll bar to 1;
     * otherwise, it calculates the scroll position based on the currentSelectedPoi's
     * distance from the left edge of the viewport and the total width of the map image.
     * ------
     * Similarly, if the currentSelectedPoi is closer to the top edge of the viewport,
     * the method sets the vertical scroll bar to 0; if it is closer to the bottom edge,
     * the method sets the vertical scroll bar to 1; otherwise, it calculates the scroll position
     * based on the currentSelectedPoi's distance from the top edge of the viewport and the total
     * height of the map image.
     * -----
     * Finally, this method sets the scroll positions of the mapPane's horizontal and vertical scroll bars
     * to the calculated values.
     */
    private void centralizeSelectedPoi() {
        if (CurrentUser.getCurrentSelectedPoi() != null) {
            Point2D windowTopLeft = WindowPointToRealPoint(new Point2D(0, 0));
            Point2D windowBottomRight = WindowPointToRealPoint(new Point2D(mapPane.getViewportBounds().getWidth(),
                    mapPane.getViewportBounds().getHeight()));
            if (CurrentUser.getCurrentSelectedPoi().getX() <= windowBottomRight.getX() &&
                    CurrentUser.getCurrentSelectedPoi().getX() >= windowTopLeft.getX()
                    && CurrentUser.getCurrentSelectedPoi().getY() <= windowBottomRight.getY() &&
                    CurrentUser.getCurrentSelectedPoi().getY() >= windowTopLeft.getY()) {
                return;
            }
            double scrollX;
            double scrollY;
            if (CurrentUser.getCurrentSelectedPoi().getX() < (windowBottomRight.getX() - windowTopLeft.getX()) / 2) {
                scrollX = 0;
            } else if (CurrentUser.getCurrentSelectedPoi().getX() > imageWidth -
                    (windowBottomRight.getX() - windowTopLeft.getX()) / 2) {
                scrollX = 1;
            } else {
                scrollX = (CurrentUser.getCurrentSelectedPoi().getX() -
                        (windowBottomRight.getX() - windowTopLeft.getX()) / 2) / (imageWidth -
                        (windowBottomRight.getX() - windowTopLeft.getX()));
            }
            if (CurrentUser.getCurrentSelectedPoi().getY() < (windowBottomRight.getY() - windowTopLeft.getY()) / 2) {
                scrollY = 0;
            } else if (CurrentUser.getCurrentSelectedPoi().getY() > imageHeight -
                    (windowBottomRight.getY() - windowTopLeft.getY()) / 2) {
                scrollY = 1;
            } else {
                scrollY = (CurrentUser.getCurrentSelectedPoi().getY() -
                        (windowBottomRight.getY() - windowTopLeft.getY()) / 2)
                        / (imageHeight - (windowBottomRight.getY() - windowTopLeft.getY()));
            }
            mapPane.setHvalue(scrollX);
            mapPane.setVvalue(scrollY);
        }
    }

    /**
     * Handles the on-click event of the Edit button.
     * Loads the MapEditingController and shows the new stage.
     * Sets the current floor map to the MapEditingController.
     * Closes the current stage.
     *
     * @param actionEvent the event triggered by clicking the Edit button
     */
    public void onEditButtonClick(ActionEvent actionEvent) {
        try {
            // Load the MapEditingController and show the new stage
            FXMLLoader fxmlLoader = returnBack("map-editing.fxml", "Map Editor Mode", actionEvent);

            // Get the MapEditingController and set the currentFloorMap
            MapEditingController mapEditingController = fxmlLoader.getController();
            mapEditingController.setCurrentFloorMap(CurrentUser.getCurrentFloorMap());


        } catch (
                IOException ex) { // thrown if there is an issue with loading the MapEditingController or closing the current stage
            throw new RuntimeException(ex);
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void selectAllLayers() {
        classrooms.setSelected(true);
        stairwells.setSelected(true);
        elevators.setSelected(true);
        entryAndExit.setSelected(true);
        genlabs.setSelected(true);
        restaurants.setSelected(true);
        cs_Labs.setSelected(true);
        collaborative.setSelected(true);
        user_POIs.setSelected(true);
    }

    public void deselectAllLayers() {
        classrooms.setSelected(false);
        stairwells.setSelected(false);
        elevators.setSelected(false);
        entryAndExit.setSelected(false);
        genlabs.setSelected(false);
        restaurants.setSelected(false);
        cs_Labs.setSelected(false);
        collaborative.setSelected(false);
        user_POIs.setSelected(false);
    }

    /**
     * This method is called when the Add POI button was clicked, it then calls the popup window
     *
     * @param actionEvent Pass the action event data along
     * @throws IOException if the FXML file for the POI popup window cannot be loaded
     */
    public void onAddPOIClicked(ActionEvent actionEvent) throws IOException {
        // Set the editor POI mode to false
        PoiPopupController.setEditMode(false);
        // Call the openPOIPopup to open the POI popup window to add a new POI.
        // Pass along if the popup is in editor mode
        openPOIPopup(actionEvent, "Add");
    }

    /**
     * This method is called when the edit POI button was clicked, it then calls the popup window
     *
     * @param actionEvent Pass the action event data along
     * @throws IOException if the FXML file for the POI popup window cannot be loaded
     */
    public void onEditPOIClicked(ActionEvent actionEvent) throws IOException {
        // Set the editor POI mode to true
        PoiPopupController.setEditMode(true);
        // Call the openPOIPopup to open the POI popup window to edit a POI.
        // Pass along if the popup is in editor mode
        openPOIPopup(actionEvent, "Edit");
    }

    /**
     * This method is called when the delete POI button was clicked, it then shows an alert asking to confirm the deletion.
     * If ok is pressed then removeSelectedPOI() is called to remove it from the user's data list, the JSON is also updated.
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    public void onDeletePOIClicked(ActionEvent actionEvent) {
        // Create an error message box
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete POI?");
        alert.setHeaderText("Warning you are about to delete the selected POI!");
        alert.setContentText("This cannot be undone! If you are okay with this press Ok. Otherwise, press Cancel.");
        // Add an exclamation graphic
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("exclamation_icon.png")));
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);
        alert.setGraphic(imageView);

        // Make the alert prevent interaction with the windows behind and force it on the same screen
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

        Optional<ButtonType> result = alert.showAndWait();
        ButtonType button = result.orElse(ButtonType.CANCEL);

        if (button == ButtonType.OK) {
            // Remove the POI from the favourites before deleting
            PointOfInterest poi = CurrentUser.getCurrentSelectedPoi();
            poi.setFavorite(false);
            FavoritePoi.setUserFavourite(poi);

            // Remove the selected POI from the user's data
            CurrentUser.removeSelectedPOI();

            // Save the list of user POIs now that the POI was removed
            CurrentUser.saveUserData();

            // Refresh the map's POIs and unselect the POI
            unselectPOIAndRefresh(actionEvent);
        }
    }

    /**
     * Opens a new window with a form to create a new Point of Interest (POI).
     * -----------
     * This method creates a new window displaying a form for the user to input
     * details about a new POI. It passes the current mouse coordinates to the
     * controller, sets the window properties (size, title, and icon), and configures
     * the stage to be displayed as a modal window. The method also sets a listener
     * to clear the pin icon when the window is closed.
     *
     * @param actionEvent the ActionEvent object representing the help button click event
     * @param mode        Set the mode for the pop-up the options are: "Edit" and "Add"
     * @throws IOException if the FXML file for the POI popup window cannot be loaded
     */
    private void openPOIPopup(ActionEvent actionEvent, String mode) throws IOException {
        // Create the new stage (window)
        Stage poiPopupStage = new Stage();

        // Load in the fxml file to a new scene and pass the stage to the controller
        FXMLLoader popupFxmlLoader = new FXMLLoader(getClass().getResource("poi-popup.fxml"));
        Scene poiPopupScene = new Scene(popupFxmlLoader.load());
        PoiPopupController.setStage(poiPopupStage);

        // Pass along the mouse position, so it can be saved as the POI location
        PoiPopupController.setCoords(coordinateX, coordinateY);

        // Set the window's title
        poiPopupStage.setTitle(mode + " POI");
        // Set the max and min heights and widths
        poiPopupStage.setMinHeight(400);
        poiPopupStage.setMinWidth(280);
        poiPopupStage.setMaxHeight(500);
        poiPopupStage.setMaxWidth(600);

        // Add the program icon to the window
        Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("western-logo.png")));
        poiPopupStage.getIcons().add(icon);

        // add a listener to the setOnHiding() method to remove the pin once the popup closes
        poiPopupStage.setOnHiding(hideEvent -> clearPinIcon());

        // Add the scene to the stage
        poiPopupStage.setScene(poiPopupScene);
        // Make the popup window block control of the main window and make it the owner
        poiPopupStage.initModality(Modality.APPLICATION_MODAL);
        poiPopupStage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        // Show the window
        poiPopupStage.show();

        // When the popup closes refresh the map's POIs and unselect the POI
        poiPopupStage.setOnHidden(e -> {
            unselectPOIAndRefresh(actionEvent);
        });
    }

    /**
     * This method is used to unselect the selected POI, clear the pin on the map,
     * refresh the POIs and refresh the informationList
     *
     * @param actionEvent the ActionEvent object representing the help button click event
     */
    private void unselectPOIAndRefresh(ActionEvent actionEvent) {
        // Remove the unselected POI
        selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), null));

        // Clear the pin and refresh the POIs
        clearPinIcon();
        refreshPOIs();

        // Call the correct method to update the informationList based on what it is populated with
        if (preventMapSelectorUpdatesFav) {
            onListFavoritesButtonClicked(actionEvent);
        } else if (preventMapSelectorUpdatesSearch) {
            onSearchButtonClicked(actionEvent);
        } else {
            setShowAllPOI();
        }
    }

    /**
     * This method is used to refresh the (user created) POIs on the map after modifying them
     */
    private void refreshPOIs() {
        CurrentUser.setMapConfig(ConfigUtil.loadMapConfig(CampusMapApplication.class.getResource("map-config.json")));

        // Find the matching current BaseMap in the MapConfig
        for (BaseMap desiredBaseMap : CurrentUser.getMapConfig().getBaseMaps()) {
            if (desiredBaseMap.equals(CurrentUser.getCurrentBaseMap())) {
                CurrentUser.setCurrentBaseMap(desiredBaseMap);
                break; // Leave the loop early
            }
        }

        // Find the matching current FloorMap in the MapConfig
        for (FloorMap desiredFloorMap : CurrentUser.getCurrentBaseMap().getFloorMaps()) {
            if (desiredFloorMap.equals(CurrentUser.getCurrentFloorMap())) {
                CurrentUser.setCurrentFloorMap(desiredFloorMap);
                break; // Leave the loop early
            }
        }

        showMap();
    }
}