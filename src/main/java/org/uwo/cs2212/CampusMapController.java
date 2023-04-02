package org.uwo.cs2212;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import org.uwo.cs2212.model.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.*;

// testing(Truman)
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.fxml.FXMLLoader;
import java.util.List;
import javafx.scene.text.*;
import static org.uwo.cs2212.CampusMapApplication.pressEnter;

/**
 * The CampusMapController class is the main controller for managing the campus map UI and
 * its interactions. It provides functionality for displaying and interacting with floor maps,
 * points of interest (POIs), and map layers. The class also enables zooming in and out, as well
 * as adding, editing, and deleting POIs. It handles UI events such as button clicks and mouse events,
 * and manages the state of the currently selected floor map and POI.
 *
 * The CampusMapController is responsible for loading floor maps and associated POI data from
 * configuration files, managing the display of map elements such as POI circles, and providing
 * methods for converting between window and real map coordinate systems. The crepresentlass uses the
 * FloorMap, PointOfInterest, ImageLayer, and SearchResult classes to  and manipulate
 * map-related data.
 *
 * @author Tingrui Zhang
 * @author Binchi Zhang
 * @author
 * @author
 * @author
 */
public class CampusMapController implements Initializable {

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


    /**
     This method is called by the FXMLLoader when initialization is complete.
     It initializes the map selector, search list view, displays all POI,
     shows the floor buttons, shows the map, and sets the favourite button state.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeMapSelector();
        initializeSearchListView();
        setShowAllPOI();
        showFloorButtons();
        showMap();
        setFavouriteButtonState();
        floorButtons = new Button[]{floor0, floor1, floor2, floor3, floor4}; //initialize the floorButtons array
        editButton.setVisible(CurrentUser.isAdmin());
    }

    /**
     This method initializes the map selector ComboBox by adding all available BaseMaps to the dropdown list.
     It sets the currentBaseMap and currentFloorMap to the first available BaseMap and its first FloorMap respectively.
     It also sets a listener to the value property of the map selector ComboBox to handle changes to the selected BaseMap.
     */
    private void initializeMapSelector() {
        for(BaseMap baseMap: CurrentUser.getMapConfig().getBaseMaps()){
            mapSelector.getItems().add(baseMap.getName());
        }
        CurrentUser.setCurrentBaseMap(CurrentUser.getMapConfig().getBaseMaps().get(0));
        CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(0));

        mapSelector.valueProperty().setValue(CurrentUser.getCurrentBaseMap().getName());
        mapSelector.valueProperty().addListener((ov, oldValue, newValue) -> {
            handleComboBoxValueChanged(ov, oldValue, newValue);

        });
    }

    /**
     Initializes the search ListView by setting a listener for changes in the selected item and calling the
     searchResultSelectionChanged method when a selection is made.
     */
    private void initializeSearchListView(){ // need to change this to a different listview (Show all poi)
        MultipleSelectionModel<SearchResult> lvSelModel = informationList.getSelectionModel();
        lvSelModel.selectedItemProperty().addListener(
                (changed, oldVal, newVal) -> {
                    searchResultSelectionChanged(changed, oldVal, newVal);
                });
        }

    /**
     * Updates the checkboxes and the informationList based on which checkboxes are selected.
     * If a checkbox is selected, show the corresponding POI types on the map and add them to the informationList.
     * If a checkbox is not selected, hide the corresponding POI types on the map.
     * Also, clear the informationList and add the relevant POIs based on the selected checkboxes.
     */
    private void checkBoxSelected(){
        informationList.getItems().clear();
        for (Layer layer: CurrentUser.getCurrentFloorMap().getLayers()){
            for(PointOfInterest poi : layer.getPoints()){
                if ((classrooms.isSelected() && poi.getType().toLowerCase().equals("classroom"))
                        || (stairwells.isSelected() && poi.getType().toLowerCase().equals("stairwell"))
                        || (elevators.isSelected() && poi.getType().toLowerCase().equals("elevator"))
                        || (entryAndExit.isSelected() && poi.getType().toLowerCase().equals("entryAndExit"))
                        || (genlabs.isSelected() && poi.getType().toLowerCase().equals("genlab"))
                        || (restaurants.isSelected() && poi.getType().toLowerCase().equals("restaurant"))
                        || (cs_Labs.isSelected() && poi.getType().toLowerCase().equals("cs_labs"))
                        || (collaborative.isSelected() && poi.getType().toLowerCase().equals("collaborative"))
                        || (user_POIs.isSelected() && poi.getType().toLowerCase().equals("user_pois"))
                ){
                    layer.setHideLayer(false);
                    informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));

                }
                if ((!classrooms.isSelected() && poi.getType().toLowerCase().equals("classroom"))
                        || (!stairwells.isSelected() && poi.getType().toLowerCase().equals("stairwell"))
                        || (!elevators.isSelected() && poi.getType().toLowerCase().equals("elevator"))
                        || (!entryAndExit.isSelected() && poi.getType().toLowerCase().equals("entryAndExit"))
                        || (!genlabs.isSelected() && poi.getType().toLowerCase().equals("genlab"))
                        || (!restaurants.isSelected() && poi.getType().toLowerCase().equals("restaurant"))
                        || (!cs_Labs.isSelected() && poi.getType().toLowerCase().equals("cs_labs"))
                        || (!collaborative.isSelected() && poi.getType().toLowerCase().equals("collaborative"))
                        || (!user_POIs.isSelected() && poi.getType().toLowerCase().equals("user_pois"))
                ){
                    layer.setHideLayer(true);
                }
            }
        }
        showMap();
    }


    public void onClassrooms(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onStairwells(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onElevators(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onEntryAndExit(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onGenlabs(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onRestaurants(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onCS_Labs(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onCollaborative(ActionEvent actionEvent){
        checkBoxSelected();
    }

    public void onUserPOIs(ActionEvent actionEvent) {
        checkBoxSelected();
    }

    /**
     * Shows or hides all points of interest based on the current filter settings. The method selects all layers and checks the corresponding checkboxes, clears the informationList, and then adds all points of interest in the current floor map to the informationList as SearchResult objects.
     */
    private void setShowAllPOI(){
        selectAllLayers();
        checkBoxSelected();
        informationList.getItems().clear();
        for (Layer layer: CurrentUser.getCurrentFloorMap().getLayers()){
            for(PointOfInterest poi : layer.getPoints()){
                informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
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
     * Handles the change event for the base map combo box. The method sets the currentBaseMap variable to the BaseMap object selected by the user, and sets the currentFloorMap variable to the first floor map in the currentBaseMap. The method then calls the showFloorButtons() and showMap() methods to display the floor buttons and the map of the current floor, respectively. Finally, the method calls the setShowAllPOI() method to show or hide all points of interest based on the current filter settings.
     * @param ov        the ObservableValue object representing the base map combo box
     * @param oldValue  the old value of the base map combo box
     * @param newValue  the new value of the base map combo box
     */
    private void handleComboBoxValueChanged(ObservableValue ov, Object oldValue, Object newValue) {
        for(BaseMap baseMap: CurrentUser.getMapConfig().getBaseMaps()) {
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
        setShowAllPOI();
    }


    /**
     * Displays the map of the current floor on the map pane. The method loads the image file for the current floor, creates an ImageView object to display the image, and adds it to a new Group object. The method then creates an ImageLayer object for each layer on the current floor, and adds it to the Group object. Finally, the Group object is added to the map pane. If an error occurs while loading the image file or creating the ImageLayer objects, the method catches the exception and returns.
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
            for(Layer layer: CurrentUser.getCurrentFloorMap().getLayers()){
                ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, layer);
                root.getChildren().add(imageLayer);
            }
            if (user_POIs.isSelected() && CurrentUser.getCurrentFloorMap().getUserLayer() != null){
                ImageLayer imageLayer = new ImageLayer(image.getWidth(), image.getHeight(), zoom, CurrentUser.getCurrentFloorMap().getUserLayer());
                root.getChildren().add(imageLayer);
            }

            mapPane.setContent(root);
        }
        catch(Exception ex)
        {}
    }

    /**
     * Handles the click event for the about button by displaying an about dialog to the user. The method creates a new stage and sets up a Label control to display information about the app and its creators. The method then displays the dialog and returns.
     * @param event the ActionEvent object representing the about button click event
     * @throws IOException if the FXML file for the about dialog cannot be loaded
     */
    @FXML
    private void aboutButtonAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        String s ="About\n\n" +
                "Western Campus Navigation App\n" +
                "Version: 1.0.0\n" +
                "Release Date: March 27, 2023\n" +
                "\n" +
                "Our Team\n" +
                "\n" +
                "  1) Boersen, Jarrett	    Student	jboerse2@uwo.ca\n"+
                "  2) Huang, Truman	    Student	yhuan939@uwo.ca\n"+
                "  3) Xie, Yaopeng	    Student	yxie447@uwo.ca\n"+
                "  4) Zhang, Binchi	    Student	bzhan484@uwo.ca\n"+
                "  5) Zhang, Tingrui	    Student	tzhan425@uwo.ca\n\n"+

        "Contact Us\n\n" +
                "  If you have any questions, feedback or suggestions,\n  please feel free to reach out to us at \n  info@campusmapapp.com.\n  We are always happy to hear from our users and \n  help you in any way we can.\n" +
                "\n" +
                "Thank you for using our Western Campus Navigation App!";
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        VBox vbox = new VBox(new Label(s));
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox, 300, 100);
        stage.setScene(scene);
        stage.setX(((Node) event.getSource()).getScene().getWindow().getX() + ((Node) event.getSource()).getScene().getWindow().getWidth() - 300);
        stage.setY(((Node) event.getSource()).getScene().getWindow().getY());
        stage.setWidth(300);
        stage.setHeight(500);
        stage.show();

    }

    /**
     * Handles the click event for the help button by displaying a help dialog to the user. The method creates a new stage and sets up a ChoiceBox control for selecting the help topic, and a Label control for displaying the help text. The method sets the initial help text based on the default value of the ChoiceBox control, and adds an event listener to the control to update the help text based on the selected value. The method then displays the dialog and returns.
     * @param event the ActionEvent object representing the help button click event
     * @throws IOException if the FXML file for the help dialog cannot be loaded
     */
    @FXML
    private void helpButtonAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());

        // Create a ChoiceBox control to select the help topic
        ChoiceBox<String> helpTopic = new ChoiceBox<>();
        helpTopic.getItems().addAll("Getting Started", "POI and Favorite","Setting");
        helpTopic.setValue("Getting Started"); // Set the default value
        //helpTopic.setPrefWidth(200);
        Label helpLabel = new Label();
        helpLabel.setWrapText(true); // Wrap text to multiple lines

        VBox vbox = new VBox(new Label("Help Page"), helpTopic, helpLabel);
        vbox.setPadding(new Insets(10));
        Scene scene = new Scene(vbox);

        stage.setScene(scene);
        stage.setWidth(300);
        stage.setHeight(500);
        stage.setX(((Node) event.getSource()).getScene().getWindow().getX() + ((Node) event.getSource()).getScene().getWindow().getWidth() - stage.getWidth() );
        stage.setY(((Node) event.getSource()).getScene().getWindow().getY());
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
                return "\n\nTo view the map, simply open the app and the campus map will be display. To search for a specific location, click on the search bar at the top of the screen and type in the name of the location you are looking for. The map will display the location and provide directions.\n\n" +
                        "To zoom in and out of the map, use the “+” and “-” zoom buttons located at the top right of the screen.\n\n" +
                        "To change the map view into a specific building, click on the layers button located at the bottom left of the screen. You can select from a variety of building, including Middlesex College, Western Science Centre and Physics and Astronomy Building\n\n";
            case "POI and Favorite":
                return "\n\nTo view the available POI, click on the menu button located at the top left of the screen and select \"Points of Interest\".\n" +
                        "\nTo view a specific POI, click on the icon on the map or select it from the list of POI.\n" +
                        "\nTo add your own POI, click on the map at the desired location and select \"Add POI\". Enter the name of the POI and select the appropriate category.\n" +
                        "\nTo view your saved POI list, click on the menu button located at the top left of the screen and select \"My POI\".\n";
            case "Setting":
                return "\n\nTo access app settings, click on the menu button located at the top left of the screen and select \"Settings\". Here, you can adjust settings such as units of measurement and language.\n" +
                        "\nTo clear your search history, click on the menu button located at the top left of the screen and select \"Clear History\".\n" +
                        "Contact Us:\n" +
                        "If you have any questions or issues with the app, please contact us at support@campusmapapp.com. We are always happy to help!\n" +
                        "\n" +
                        "Thank you for using our campus map viewing app. We hope this help menu has been helpful in navigating the app. If you have any feedback or suggestions, please don't hesitate to reach out to us.\n" +
                        "\n";
            default:
                return "";
        }
    }

    /**
     * Handles the sign-out button click event by displaying a confirmation dialog to the user. The dialog asks if the user wants to save changes to the user account data, and provides options to save, not save, or cancel. The method sets up event handlers for the buttons, and takes appropriate action based on the user's choice. If the user chooses to save, the method saves the user account data and returns to the login page. If the user chooses not to save, the method returns to the login page without saving. If the user chooses to cancel, the method closes the confirmation dialog and returns.
     * @param event the ActionEvent object representing the sign-out button click event
     * @throws IOException if the FXML file for the login view cannot be loaded
     */
    @FXML
    private void signOut(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        String s ="";
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());

        // Create three buttons
        Button saveButton = new Button("Save");
        saveButton.setPrefSize(60, 30); // Set button size
        saveButton.setTranslateX(100); // Set button X position
        Button dontSaveButton = new Button("Don't save");
        dontSaveButton.setPrefSize(90, 30); // Set button size
        dontSaveButton.setTranslateX(20); // Set button X position
        Button cancelButton = new Button("Cancel");
        cancelButton.setPrefSize(60, 30); // Set button size
        cancelButton.setTranslateX(180); // Set button X position

        // Add event handlers to the buttons
        saveButton.setOnAction(e -> {
            // Handle yes button click
            try {
                UserList userlist = ConfigUtil.loadUserList(CampusMapApplication.class.getResource("user-account.json"));
                // properly save the data
                ConfigUtil.saveUserList(userlist,CampusMapApplication.class.getResource("user-account.json"));
                returnBack("login-view.fxml","Login Page");
                ((Node)(event.getSource())).getScene().getWindow().hide();
                stage.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        dontSaveButton.setOnAction(e -> {
            // Handle no button click
            //Don't save the data, jump back to login page
            try {
                //now "enter" key can be user when re-login
                    FXMLLoader loginFxmlLoader = returnBack("login-view.fxml", "Login Page");
                    Stage loginStage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                    Scene loginScene = loginStage.getScene();
                    pressEnter(loginFxmlLoader, loginScene);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            ((Node)(event.getSource())).getScene().getWindow().hide();
            stage.close();
        });
        cancelButton.setOnAction(e -> {
            // Handle cancel button click
            stage.close();
        });
        // Create an HBox container to hold the buttons
        HBox hbox = new HBox( dontSaveButton, saveButton, cancelButton);
        hbox.setTranslateY(50);
        hbox.setSpacing(-5); // Set spacing between buttons
        VBox vbox = new VBox(new Label(s), hbox); // Add HBox to VBox container
        Scene scene = new Scene(vbox, 280, 100);
        stage.setScene(scene);
        stage.setX(((Node) event.getSource()).getScene().getWindow().getX() + ((Node) event.getSource()).getScene().getWindow().getWidth()-650);
        stage.setY(((Node) event.getSource()).getScene().getWindow().getY()+200);
        stage.setWidth(400);
        stage.setHeight(200);
        stage.setTitle("Sign Out"); // Set the title of the pop-up window
        stage.show();
    }

    /**
     * Returns the user to the previous view by loading the FXML file and creating a new stage for it. The method sets the stage for the LoginViewController and creates a new scene with the specified FXML file and dimensions. If the FXML file is for the login view, the method also sets up a key event handler to handle the Enter key and log the user in. The method returns an FXMLLoader object for the loaded FXML file.
     * @param file the name of the FXML file to be loaded
     * @param title the title of the new stage
     * @return an FXMLLoader object for the loaded FXML file
     * @throws IOException if the FXML file cannot be loaded
     */
    @FXML
    private FXMLLoader returnBack(String file, String title) throws IOException {
        int v = 1080;
        int v1 = 800;


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
            scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
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
        stage.setResizable(false);
        stage.setX(200);
        stage.setY(70);
        stage.show();

        return fxmlLoader;
    }


    @FXML
    private void onZoomInButtonClicked(ActionEvent actionEvent) {
        if (zoom > 0.5){
            zoom *= 0.8;
        }
        else{
            zoomIn.setDisable(false);
        }
        showMap();
    }

    @FXML
    private void onZoomOutButtonClicked(ActionEvent actionEvent) {
        if (zoom < 1.7){
            zoom *= 1.2;
        }
        else{
            zoomOut.setDisable(false);
        }
        showMap();
    }

    @FXML
    private void onZoomResetButtonClicked(ActionEvent actionEvent) {
        zoom = 1;
        showMap();
    }

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
        showMap();

        Button clickedButton = (Button) actionEvent.getSource();
        highlightSelectedFloorButton(clickedButton);
    }



    @FXML
    private void onFloor1ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 2){
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(1));
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    @FXML
    private void onFloor2ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 3){
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(2));
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    @FXML
    private void onFloor3ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 4){
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(3));
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    @FXML
    private void onFloor4ButtonClick(ActionEvent actionEvent) {
        if (CurrentUser.getCurrentBaseMap().getFloorMaps().size() >= 5){
            CurrentUser.setCurrentFloorMap(CurrentUser.getCurrentBaseMap().getFloorMaps().get(4));
            showMap();

            Button clickedButton = (Button) actionEvent.getSource();
            highlightSelectedFloorButton(clickedButton);
        }
    }

    private void showFloorButtons(){
        int floorCount = CurrentUser.getCurrentBaseMap().getFloorMaps().size();
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

    /**
     * Handles a mouse click event on the map. This method calculates the real-world mouse position based on the mouse event, and then iterates through all points of interest on the current floor map to determine if the mouse click hits a point of interest. If a point of interest is hit, the method selects the point of interest, shows it in the informationList ListView, and returns. If no point of interest is hit, the method deselects any currently selected POI and returns.
     * @param mouseEvent the MouseEvent object representing the mouse click event
     */
    public void onMapClicked(MouseEvent mouseEvent) {
        Point2D realMousePosition = calculateRealMousePosition(mouseEvent);
        for(Layer layer: CurrentUser.getCurrentFloorMap().getLayers()){
            for(PointOfInterest poi: layer.getPoints()) {
                if (hitTest(realMousePosition, poi)) {
                    selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));

                    //pop-up window
                    Stage stage = new Stage();
                    String s = "Name:" + "   " + poi.getName() + "\nType:    " + poi.getType() + "\nDescription:" + "  " + poi.getDescription();
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.initOwner((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow());
                    Label label = new Label(s);
                    label.setStyle("-fx-font-size: 12px; -fx-text-fill: #333333;");
                    VBox vbox = new VBox(label);
                    stage.setTitle(poi.getName());
                    vbox.setStyle("-fx-background-color: #d9eff2;");
                    vbox.setPadding(new Insets(10));
                    Scene scene = new Scene(vbox, 300, 100);
                    stage.setScene(scene);

                    // Calculate the window position of the POI
                    Point2D poiRealPoint = new Point2D(poi.getX(), poi.getY());
                    Point2D poiWindowPoint = WindowPointToRealPoint(poiRealPoint);
                    //
                    Node node = (Node) mouseEvent.getSource();
                    Point2D poiPosition = new Point2D(poi.getX(),poi.getY());
                    Point2D poiScreenPosition = node.localToScreen(poiPosition);

                    // Set the pop-up window position
                    double popUpWindowWidth = 200;
                    double popUpWindowHeight = 170;
                    double popUpWindowX = poiScreenPosition.getX() - popUpWindowWidth / 2;
                    double popUpWindowY = poiScreenPosition.getY() - popUpWindowHeight / 2;

                    stage.setX(popUpWindowX);
                    stage.setY(popUpWindowY);

                    stage.setWidth(200);
                    stage.setHeight(170);
                    stage.show();
                    showPoiInList(poi);
                    return;
                }
            }
        }
        if (CurrentUser.getCurrentFloorMap().getUserLayer() != null && user_POIs.isSelected()){
            for(PointOfInterest poi: CurrentUser.getCurrentFloorMap().getUserLayer().getPoints()) {
                if (hitTest(realMousePosition, poi)) {
                    selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
                    showPoiInList(poi);
                    return;
                }
            }
        }
        selectPoi(new SearchResult(CurrentUser.getCurrentFloorMap(), null));
    }

    /**
     * Selects a Point of Interest (POI) based on the given SearchResult object. The method sets the currentFloorMap to the floor map associated with the SearchResult object. It also updates the selected state of the currentSelectedPoi and the newly selected POI. If the newly selected POI is not within the viewport, this method centralizes the POI on the map. It then shows the map, sets the favourite button state, and returns.
     * @param searchResult the SearchResult object containing the POI to be selected
     * @throws NullPointerException if the SearchResult object is null
     */
    private void selectPoi(SearchResult searchResult){
        if(searchResult != null){
            CurrentUser.setCurrentFloorMap(searchResult.getFloorMap());
            if(CurrentUser.getCurrentSelectedPoi() != null){
                CurrentUser.getCurrentSelectedPoi().setSelected(false);
            }
            CurrentUser.setCurrentSelectedPoi(searchResult.getPoi());
            if(CurrentUser.getCurrentSelectedPoi() != null){
                CurrentUser.getCurrentSelectedPoi().setSelected(true);
            }
            centralizeSelectedPoi();
            showMap();
            setFavouriteButtonState();
        }
    }

    private void showPoiInList(PointOfInterest poi) {
        informationList.getItems().clear();
        informationList.getItems().add(new SearchResult(CurrentUser.getCurrentFloorMap(), poi));
    }


    private boolean hitTest(Point2D mousePosition, PointOfInterest poi){
        if (mousePosition.getX() <= poi.getX()+6/zoom && mousePosition.getX() >= poi.getX()-6/zoom && mousePosition.getY() <= poi.getY()+6/zoom && mousePosition.getY() >= poi.getY()-6/zoom){
            return true;
        }
        return false;
    }

    /**
     Converts a point in the window coordinate system to a point in the real map coordinate system.
     Takes into account the current zoom level and scroll position.
     @param windowPoint The point in the window coordinate system
     @return The converted point in the real map coordinate system
     */
    private Point2D WindowPointToRealPoint(Point2D windowPoint){
        double windowXValue = (imageWidth - mapPane.getViewportBounds().getWidth()/zoom) * mapPane.getHvalue();
        double windowYValue = (imageHeight - mapPane.getViewportBounds().getHeight()/zoom) * mapPane.getVvalue();
        System.out.println("windowPosition:(" + windowXValue + ", " + windowYValue+")");
        double mouseX = windowXValue + windowPoint.getX()/zoom;
        double mouseY = windowYValue + windowPoint.getY()/zoom;
        if (mapPane.getViewportBounds().getHeight() >= imageHeight){
            mouseY = windowPoint.getY()/zoom;
        }
        if (mapPane.getViewportBounds().getWidth() >= imageWidth){
            mouseX = windowPoint.getX()/zoom;
        }
        System.out.println("mouse real position:(" + mouseX + ", " + mouseY+")");
        return new Point2D(mouseX, mouseY);
    }

    private Point2D calculateRealMousePosition(MouseEvent mouseEvent){
        return WindowPointToRealPoint(new Point2D(mouseEvent.getX(), mouseEvent.getY()));
    }

    /**
     * This method is called when the search button is clicked. It searches for the text entered in the search text field in
     * all the layers of the current base map and adds the search results to the informationList ListView. If the text field
     * is empty, no search results are displayed.
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    public void onSearchButtonClicked(ActionEvent actionEvent) {

        selectAllLayers();
        checkBoxSelected();
        String text = searchText.getText().toLowerCase().trim();
        if (!text.equals("")){
            informationList.getItems().clear();
            for(FloorMap floorMap : CurrentUser.getCurrentBaseMap().getFloorMaps()){
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

    /**
     * Checks if a string contains a target string, ignoring case.
     *
     * @param target the target string to search for
     * @param text the text string to search in
     * @return true if the target string is found in the text string, false otherwise
     */
    private static boolean contains(String target, String text){
        if(target != null && target.toLowerCase().contains(text)){
            return true;
        }
        return false;
    }

    /**
     Sets the state of the favorite button based on the current selected point of interest.
     If there is no selected point of interest, the favorite button is disabled and set to the default state (not a favorite).
     If there is a selected point of interest, the favorite button is enabled and set to the appropriate state (favorite or not favorite)
     based on the selected point of interest's favorite status.
     */
    private void setFavouriteButtonState() {
        ImageView imageView;
        if (CurrentUser.getCurrentSelectedPoi() != null) {
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

    public void onFavoriteButtonClicked(ActionEvent actionEvent) {
        selectAllLayers();
        if(CurrentUser.getCurrentSelectedPoi() != null){
            CurrentUser.getCurrentSelectedPoi().setFavorite(!CurrentUser.getCurrentSelectedPoi().isFavorite());
            setFavouriteButtonState();
        }
    }

    public void onClearButtonClicked(ActionEvent actionEvent) {
        deselectAllLayers();
        checkBoxSelected();
        searchText.setText("");
        informationList.getItems().clear();
    }

    public void onListFavoritesButtonClicked(ActionEvent actionEvent) {
        informationList.getItems().clear();
        for (BaseMap baseMap : CurrentUser.getMapConfig().getBaseMaps()){
            for (FloorMap floorMap : baseMap.getFloorMaps()){
                for (Layer layer: floorMap.getLayers()){
                    for(PointOfInterest poi : layer.getPoints()){
                        if (poi.isFavorite()) {
                            informationList.getItems().add(new SearchResult(floorMap, poi));
                        }
                    }
                }
            }
        }
    }

    /**
     * Centralizes the currently selected Point of Interest (POI) on the map by scrolling the view to show the POI in the center of the screen.
     * If the currentSelectedPoi is null, this method does nothing.
     * If the currentSelectedPoi is not null and lies within the viewport, this method returns without doing anything.
     *
     * If the currentSelectedPoi is not null and lies outside the viewport,
     * this method calculates the desired scroll position for both the horizontal
     * and vertical scroll bars based on the currentSelectedPoi's position and the
     * dimensions of the viewport and the map image. If the currentSelectedPoi is
     * closer to the left edge of the viewport, the method sets the horizontal scroll bar to 0;
     * if it is closer to the right edge, the method sets the horizontal scroll bar to 1;
     * otherwise, it calculates the scroll position based on the currentSelectedPoi's
     * distance from the left edge of the viewport and the total width of the map image.
     *
     * Similarly, if the currentSelectedPoi is closer to the top edge of the viewport,
     * the method sets the vertical scroll bar to 0; if it is closer to the bottom edge,
     * the method sets the vertical scroll bar to 1; otherwise, it calculates the scroll position
     * based on the currentSelectedPoi's distance from the top edge of the viewport and the total height of the map image.
     *
     * Finally, this method sets the scroll positions of the mapPane's horizontal and vertical scroll bars
     * to the calculated values. It then loops through all the points of interest that are marked as
     * favorites in the currentFloorMap's layers and adds them to the informationList ListView.
     * @throws NullPointerException if any of the objects referenced within the method are null.
     */
    private void centralizeSelectedPoi(){
        if(CurrentUser.getCurrentSelectedPoi() != null){
            Point2D windowTopLeft = WindowPointToRealPoint(new Point2D(0, 0));
            Point2D windowBottomRight = WindowPointToRealPoint(new Point2D(mapPane.getViewportBounds().getWidth(), mapPane.getViewportBounds().getHeight()));
            if (CurrentUser.getCurrentSelectedPoi().getX() <= windowBottomRight.getX() && CurrentUser.getCurrentSelectedPoi().getX() >= windowTopLeft.getX()
                    && CurrentUser.getCurrentSelectedPoi().getY() <= windowBottomRight.getY() && CurrentUser.getCurrentSelectedPoi().getY() >= windowTopLeft.getY()){
                return;
            }
            double scrollX = 0;
            double scrollY = 0;
            if(CurrentUser.getCurrentSelectedPoi().getX() < (windowBottomRight.getX()-windowTopLeft.getX())/2){
                scrollX = 0;
            }
            else if (CurrentUser.getCurrentSelectedPoi().getX() > imageWidth-(windowBottomRight.getX()-windowTopLeft.getX())/2){
                scrollX = 1;
            }
            else{
                scrollX = (CurrentUser.getCurrentSelectedPoi().getX() - (windowBottomRight.getX()-windowTopLeft.getX())/2)/(imageWidth-(windowBottomRight.getX()-windowTopLeft.getX()));
            }
            if(CurrentUser.getCurrentSelectedPoi().getY() < (windowBottomRight.getY()-windowTopLeft.getY())/2){
                scrollY = 0;
            }
            else if (CurrentUser.getCurrentSelectedPoi().getY() > imageHeight-(windowBottomRight.getY()-windowTopLeft.getY())/2){
                scrollY = 1;
            }
            else{
                scrollY = (CurrentUser.getCurrentSelectedPoi().getY() - (windowBottomRight.getY()-windowTopLeft.getY())/2)/(imageHeight-(windowBottomRight.getY()-windowTopLeft.getY()));
            }
            mapPane.setHvalue(scrollX);
            mapPane.setVvalue(scrollY);
        }
        for (Layer layer: CurrentUser.getCurrentFloorMap().getLayers()){
            for(PointOfInterest poi : layer.getPoints()){
                if (poi.isFavorite()) {
                    informationList.getItems().add(poi);
                }
            }
        }
    }

    /**
     Handles the on-click event of the Edit button.
     Loads the MapEditingController and shows the new stage.
     Sets the current floor map to the MapEditingController.
     Closes the current stage.
     @param actionEvent the event triggered by clicking the Edit button
     @throws IOException if there is an issue with loading the MapEditingController or closing the current stage
     */
    public void onEditButtonClick(ActionEvent actionEvent) throws IOException {
        try {
            // Load the MapEditingController and show the new stage
            FXMLLoader fxmlLoader = returnBack("map-editing.fxml", "Map Editing Mode");

            // Get the MapEditingController and set the currentFloorMap
            MapEditingController mapEditingController = fxmlLoader.getController();
            mapEditingController.setCurrentFloorMap(CurrentUser.getCurrentFloorMap());


        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
    }

    public void selectAllLayers(){
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

    public void deselectAllLayers(){
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

    public void onAddPOIClicked(ActionEvent actionEvent) {
        // Create a new stage to be a popup window and set the owner to the CampusMap view
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());
        // Set the title of the popup window
        stage.setTitle("New POI");

        // name
        // room number
        // favourite
        // description

        // set the type as well to user created

        // Create the labels and text-fields and put them in a corresponding vbox
        Label nameLabel = new Label("Enter a name for the POI");
        TextField name = new TextField();
        name.setPromptText("Name");
        VBox nameBox = new VBox(nameLabel, name);

        Label roomNumberLabel = new Label("Enter a room number for the POI");
        TextField roomNumber = new TextField();
        roomNumber.setPromptText("Room number");
        VBox roomNumberBox = new VBox(roomNumberLabel, roomNumber);

        Label descriptionLabel = new Label("Enter a description for the POI");
        TextField description = new TextField();
        description.setPromptText("Description");
        VBox descriptionBox = new VBox(descriptionLabel, description);

        // Get the favourite icon and create a new image view for it
        ImageView favouriteIcon = new ImageView(getClass().getResource("favorite1.png").toExternalForm());
        favouriteIcon.setFitWidth(16);
        favouriteIcon.setFitHeight(16);

        // Create the favourite button
        Button favourite = new Button("Set POI as a favourite", favouriteIcon); // TODO: Change icon on click

        // Create the favourite button
        Button savePOI = new Button("Save POI");

        // Add a Vbox so the items automatically are laid out in the window
        // Add all the elements and separate them with a spacer which is a blank label
        VBox vbox = new VBox(nameBox, new Label(), roomNumberBox, new Label(), descriptionBox, new Label(), favourite, new Label(), savePOI);
        // Set the padding and create the scene
        vbox.setPadding(new Insets(10, 10, 10, 10));
        Scene scene = new Scene(vbox);

        // Add the scene to the stage (window) and set the size
        stage.setScene(scene);
        stage.setWidth(300);
        stage.setHeight(500);
        stage.setResizable(false);

        stage.show();


    }

}