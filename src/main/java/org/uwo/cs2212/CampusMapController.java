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
import java.util.*;
import java.awt.*;

// testing(Truman)
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;

import javafx.fxml.FXMLLoader;
import java.io.IOException;
import javafx.scene.text.*;





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
    private Button floor5;
    @FXML
    private Button poiToggleButton;
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
    private Label helpLabel;
    @FXML Button about;
    @FXML
    private ComboBox mapSelector;
    @FXML
    private TextField searchText;
    @FXML
    private ListView informationList;
    @FXML
    private ScrollPane mapPane;
    @FXML
    private CheckBox Classrooms;
    @FXML
    private CheckBox Stairwells;
    @FXML
    private CheckBox Elevators;
    @FXML
    private CheckBox Washrooms;
    @FXML
    private CheckBox EntryAndExit;
    @FXML
    private CheckBox Genlabs;
    @FXML
    private CheckBox Restaurants;
    @FXML
    private CheckBox CS_Labs;
    @FXML
    private CheckBox Collaborative;

    private double zoom = 1.0;
    private double imageWidth;
    private double imageHeight;
    private PointOfInterest currentSelectedPoi;
    private MapConfig mapConfig;
    private BaseMap currentBaseMap;
    private FloorMap currentFloorMap;
    LinkedList classRoom = new LinkedList();
    LinkedList stairWells = new LinkedList();
    LinkedList elevators = new LinkedList();
    LinkedList washRooms = new LinkedList();
    LinkedList entryAndExit = new LinkedList();
    LinkedList genLabs = new LinkedList();
    LinkedList restaurants = new LinkedList();
    LinkedList cs_Labs = new LinkedList();
    LinkedList collaborative = new LinkedList();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mapConfig = ConfigUtil.loadMapConfig(CampusMapApplication.class.getResource("map-config.json"));
        initializeMapSelector();
        initializeSearchListView();
        setShowAllPOI();
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

    private void initializeSearchListView(){ // need to change this to a different listview (Show all poi)
        MultipleSelectionModel<SearchResult> lvSelModel = informationList.getSelectionModel();
        lvSelModel.selectedItemProperty().addListener(
                (changed, oldVal, newVal) -> {
                    searchResultSelectionChanged(changed, oldVal, newVal);
                });
    }

    public void onClassrooms(ActionEvent actionEvent){
        if (Classrooms.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("ClassRoom")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        classRoom.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(classRoom.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(classRoom.get(i));
            }
        }
    }

    public void onStairwells(ActionEvent actionEvent){
        if (Stairwells.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("StairWell")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        stairWells.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(stairWells.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(stairWells.get(i));
            }
        }
    }

    public void onElevators(ActionEvent actionEvent){
        if (Elevators.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("Elevator")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        elevators.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(elevators.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(elevators.get(i));
            }
        }
    }

    public void onWashrooms(ActionEvent actionEvent){
        if (Washrooms.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("WashRoom")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        washRooms.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(washRooms.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(washRooms.get(i));
            }
        }
    }

    public void onEntryAndExit(ActionEvent actionEvent){
        if (EntryAndExit.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("EntryAndExit")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        entryAndExit.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(entryAndExit.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(entryAndExit.get(i));
            }
        }
    }

    public void onGenlabs(ActionEvent actionEvent){
        if (Genlabs.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("GenLab")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        genLabs.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(genLabs.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(genLabs.get(i));
            }
        }
    }

    public void onRestaurants(ActionEvent actionEvent){
        if (Restaurants.isSelected()){
            //informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("Restaurant")){
                        restaurants.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(restaurants.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(restaurants.get(i));
            }
        }
    }

    public void onCS_Labs(ActionEvent actionEvent){
        if (CS_Labs.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("CS_Labs")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        cs_Labs.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(cs_Labs.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(cs_Labs.get(i));
            }
        }
    }

    public void onCollaborative(ActionEvent actionEvent){
        if (Collaborative.isSelected()){
            informationList.getItems().clear(); // change that later
            for (Layer layer: currentFloorMap.getLayers()){
                for(PointOfInterest poi : layer.getPoints()){
                    if (poi.getType().equals("Collaborative")){
                        //informationList.getItems().add(new SearchResult(currentFloorMap, poi));
                        collaborative.add(new SearchResult(currentFloorMap, poi));
                    }
                }
            }
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().add(collaborative.get(i));
            }
        } else {
            for (int i = 0; i < classRoom.size(); i++){
                informationList.getItems().remove(collaborative.get(i));
            }
        }
    }

    private void setShowAllPOI(){
        informationList.getItems().clear();
        for (Layer layer: currentFloorMap.getLayers()){
            for(PointOfInterest poi : layer.getPoints()){
                informationList.getItems().add(new SearchResult(currentFloorMap, poi));
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

    private void handleComboBoxValueChanged(ObservableValue ov, Object oldValue, Object newValue) {
        for(BaseMap baseMap: mapConfig.getBaseMaps()){
            if (newValue.toString().equals(baseMap.getName())){
                currentBaseMap = baseMap;
                currentFloorMap = currentBaseMap.getFloorMaps().get(0);
                showFloorButtons();
                showMap();
            }
		}
        setShowAllPOI();
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
    private void onSettingsButtonClicked(ActionEvent actionEvent) { // delete this later
        System.out.println("operate successful.");
    }
    /** option*/
    @FXML
    private void onHelpButtonClicked(ActionEvent actionEvent) {
        try {
            URL configUrl = getClass().getResource("help_temp.pdf");
            Desktop desk = Desktop.getDesktop();
            desk.browse(configUrl.toURI());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void aboutButtonAction(ActionEvent event) throws IOException {
        Stage stage = new Stage();
        String s ="About\n\n" +
                "Campus Map Viewing App\n" +
                "Version: 1.0.0\n" +
                "Release Date: March 27, 2023\n" +
                "\n" +
                "Our Team\n" +
                "\n" +
                "  1) Boersen, Jarrett	Student	jboerse2@uwo.ca\n"+
                "  2) Huang, Truman	    Student	yhuan939@uwo.ca\n"+
                "  3) Xie, Yaopeng	    Student	yxie447@uwo.ca\n"+
                "  4) Zhang, Binchi	    Student	bzhan484@uwo.ca\n"+
                "  5) Zhang, Tingrui	    Student	tzhan425@uwo.ca\n\n"+

        "Contact Us\n\n" +
                "  If you have any questions, feedback or suggestions,\n  please feel free to reach out to us at \n  info@campusmapapp.com.\n  We are always happy to hear from our users and \n  help you in any way we can.\n" +
                "\n" +
                "Thank you for using our campus map viewing app!";
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner((Stage) ((Node) event.getSource()).getScene().getWindow());
        VBox vbox = new VBox(new Label(s));
        Scene scene = new Scene(vbox, 300, 100);
        stage.setScene(scene);
        stage.setX(((Node) event.getSource()).getScene().getWindow().getX() + ((Node) event.getSource()).getScene().getWindow().getWidth() - 300);
        stage.setY(((Node) event.getSource()).getScene().getWindow().getY());
        stage.setWidth(300);
        stage.setHeight(500);
        stage.show();
    }


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
                return "Getting started:\n\nTo view the map, simply open the app and the campus map will be display. To search for a specific location, click on the search bar at the top of the screen and type in the name of the location you are looking for. The map will display the location and provide directions.\n\n" +
                        "To zoom in and out of the map, use the “+” and “-” zoom buttons located at the top right of the screen.\n\n" +
                        "To change the map view into a specific building, click on the layers button located at the bottom left of the screen. You can select from a variety of building, including Middlesex College, Western Science Centre and Physics and Astronomy Building\n\n";
            case "POI and Favorite":
                return "POI and Favorite:\n\nTo view the available POI, click on the menu button located at the top left of the screen and select \"Points of Interest\".\n" +
                        "\nTo view a specific POI, click on the icon on the map or select it from the list of POI.\n" +
                        "\nTo add your own POI, click on the map at the desired location and select \"Add POI\". Enter the name of the POI and select the appropriate category.\n" +
                        "\nTo view your saved POI list, click on the menu button located at the top left of the screen and select \"My POI\".\n";
            case "Setting":
                return "Setting:\n\nTo access app settings, click on the menu button located at the top left of the screen and select \"Settings\". Here, you can adjust settings such as units of measurement and language.\n" +
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
                returnBack("login-view.fxml","Login");
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
                returnBack("login-view.fxml","Login");
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
        stage.show();
    }

    @FXML
    private void returnBack(String file,String title) throws IOException {
        Stage stage=new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(CampusMapApplication.class.getResource(file));
        Scene scene = new Scene(fxmlLoader.load(), 571, 400);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setX(200);
        stage.setY(70);
        stage.show();
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