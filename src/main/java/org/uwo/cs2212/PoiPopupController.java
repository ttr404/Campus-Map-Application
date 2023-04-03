package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.uwo.cs2212.model.BaseMap;
import org.uwo.cs2212.model.FloorMap;
import org.uwo.cs2212.model.PointOfInterest;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// TODO:    * Name
//          * Room number
//          * Favourite
//          * Description
//          * Set the type as well to user created

public class PoiPopupController implements Initializable {
    // The elements in the window
    public TextField NameField;
    public TextField RoomNumberField;
    public TextArea DescriptionField;
    public Button favouriteButton;
    public Button SaveButton;

    /**
     * This variable is used to store if the new POI should be marked as a favourite.
     * It starts off as true so when setFavouriteButtonState() is called during initialization
     * it will be left as false and show the icon as the false one.
     */
    boolean favourite = true;
    /**
     * This variable is used to store the x coordinate of the POI
     */
    private static double xCoord;
    /**
     * This variable is used to store the y coordinate of the POI
     */
    private static double yCoord;
    /**
     * This variable is used to store the stage for the popup view
     */
    private static Stage stage;

    /**
     * This variable is used to store the current base map so the POI
     * can be saved based on the base map
     */
    private static BaseMap currentBaseMap;
    /**
     * This variable is used to store the current floor map so the POI
     * can be saved based on the floor map
     */
    private static FloorMap currentFloopMap;

    /**
     * This method called automatically by javafx and is used to load some things when window is initialized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setFavouriteButtonState();
    }

    /**
     * Set the stage for the POI popup view.
     *
     * @param stage a Stage object representing the stage for the login view
     */
    public static void setStage(Stage stage) {
        PoiPopupController.stage = stage;
    }

    /**
     * This method is used to set the coordinates of the POI being added
     *
     * @param xCoord The x coordinates to be saved for the POI
     * @param yCoord The y coordinates to be saved for the POI
     */
    public static void setCoords(double xCoord, double yCoord) {
        PoiPopupController.xCoord = xCoord;
        PoiPopupController.yCoord = yCoord;
    }

    /**
     * This method is used to set the current BaseMap (which building/campus map is active)
     *
     * @param currentBaseMap The BaseMap object to be stored
     */
    public static void setCurrentBaseMap(BaseMap currentBaseMap) {
        PoiPopupController.currentBaseMap = currentBaseMap;
    }

    /**
     * This method is used to set the current FloopMap (which floor of the BaseMap is active)
     *
     * @param currentFloopMap The FloopMap object to be stored
     */
    public static void setCurrentFloopMap(FloorMap currentFloopMap) {
        PoiPopupController.currentFloopMap = currentFloopMap;
    }

    /**
     * This method is called when the favourite button is clicked which calls the setFavouriteButtonState
     * method to toggle the favourite state
     *
     * @param actionEvent
     */
    public void OnFavouriteClicked(ActionEvent actionEvent) {
        setFavouriteButtonState();
    }

    /**
     * This method is called when the save button is clicked this collects all the data the user
     * entered then runs the save method
     *
     * @param actionEvent
     */
    public void OnSaveClicked(ActionEvent actionEvent) {
        // If the user didn't enter the required information inform them
        if (NameField.getText().equals("") || RoomNumberField.getText().equals("")) {
            // Create an error message box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save POI");
            alert.setHeaderText("Unable to save POI!");
            alert.setContentText("Some of the required POI information was missing. To save it please enter both a name and room Number.");
            alert.showAndWait();
        } else { // Otherwise, save and close the Add POI pop-up
            save(NameField.getText(), RoomNumberField.getText(), DescriptionField.getText());

            stage.close();
        }
    }

    /**
     * This method is used to create a POI using the given information and call the required helper methods to save it
     *
     * @param name The name of the POI
     * @param roomNumber The room number for the POI
     * @param description The description for the POI
     */
    private void save(String name, String roomNumber, String description) {
        // Create a new POI
        PointOfInterest poi = new PointOfInterest();

        // Add the information
        poi.setName(name);
        poi.setRoomNumber(roomNumber);
        poi.setDescription(description);
        poi.setFavorite(favourite);
        poi.setX(xCoord);
        poi.setY(yCoord);
        // Set the type for the POI
        poi.setType("User");

        // Call the method to add the POI to the current user's data object
        CurrentUser.addPoi(CurrentUser.getCurrentBaseMap(), CurrentUser.getCurrentFloorMap(), poi);
        // Save the object to a JSON file
        CurrentUser.saveUserData();
    }

    /**
     * This method is used to change of the favourite button image and store if the user marked the POI as a favourite
     * by switching between true and false
     */
    private void setFavouriteButtonState() {
        // Store the imageview for the button
        ImageView imageView;

        // Not favourite to switch it between true and false
        favourite = !favourite;

        // If favourite is true show a coloured in icon
        if (favourite) {
            imageView = new ImageView(getClass().getResource("favorite1.png").toExternalForm());
        } else { // Otherwise, grey out the icon
            imageView = new ImageView(getClass().getResource("favorite0.png").toExternalForm());
        }

        // Set the size of the imageview and set it as the button's graphic
        imageView.setFitWidth(16);
        imageView.setFitHeight(16);
        favouriteButton.setGraphic(imageView);
    }
}
