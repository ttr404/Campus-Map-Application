package org.uwo.cs2212;

import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uwo.cs2212.model.FavoritePoi;
import org.uwo.cs2212.model.PointOfInterest;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is used to handle the functionality of the POI popup adder/editor. It always gets information from the
 * user via the text fields and buttons, however, in edit mode it starts off with the POI's data being edited. It calls
 * other external classes that implement the saving and editing functionality.
 *
 * @author Jarrett Boersen
 */
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
     * This variable is sued to store if the POI popup is in edit mode
     */
    private static boolean editMode;
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
     * This method called automatically by javafx and is used to load some things when window is initialized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // If editMode is true then set the fields
        if (editMode) {
            // Set the filed text based on the selected POI in CurrentUser
            setFields();
            // Switch the value, so it will be correct after setFavouriteButtonState is called
            favourite = !favourite;
        }
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
     * This method is used to set the state of the editMode
     *
     * @param editMode The value set editMode to
     */
    public static void setEditMode(boolean editMode) {
        PoiPopupController.editMode = editMode;
    }

    /**
     * This method is called when the favourite button is clicked which calls the setFavouriteButtonState
     * method to toggle the favourite state
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    public void OnFavouriteClicked(ActionEvent actionEvent) {
        setFavouriteButtonState();
    }

    /**
     * This method is called when the save button is clicked this collects all the data the user
     * entered then runs the save method
     *
     * @param actionEvent an ActionEvent object representing the click event
     */
    public void OnSaveClicked(ActionEvent actionEvent) {
        // If the user didn't enter the required information inform them
        if (NameField.getText().equals("") || RoomNumberField.getText().equals("")) {
            // Create an error message box
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Save POI");
            alert.setHeaderText("Unable to save POI!");
            alert.setContentText("Some of the required POI information was missing. To save it please enter both a name and room Number.");
            // Add an error graphic
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("error_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);

            alert.initModality(Modality.APPLICATION_MODAL);
            alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

            alert.showAndWait();
        } else { // Otherwise, save and close the Add POI pop-up
            // If editMode is true then call the method to save the edited POI
            if (editMode) {
                edit(NameField.getText(), RoomNumberField.getText(), DescriptionField.getText(), actionEvent);
            } else { // Otherwise, call the method to save the POI
                save(NameField.getText(), RoomNumberField.getText(), DescriptionField.getText(), actionEvent);
            }

            stage.close();
        }
    }

    /**
     * This method is used to create a POI using the given information and call the required helper methods to save it
     *
     * @param name        The name of the POI
     * @param roomNumber  The room number for the POI
     * @param description The description for the POI
     * @param actionEvent an ActionEvent object representing the click event
     */
    private void save(String name, String roomNumber, String description, ActionEvent actionEvent) {
        // Used to store if the user data saved successfully
        boolean saveSuccessful;

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
        poi.setType("User POI");

        // Call the method to add the POI to the current user's data object
        CurrentUser.addPoi(CurrentUser.getCurrentBaseMap(), CurrentUser.getCurrentFloorMap(), poi);
        // Save the object to a JSON file and store if the method saved successfully
        saveSuccessful = CurrentUser.saveUserData();

        // Set the POI as a favourite or not for the user
        FavoritePoi.setUserFavourite(poi);

        // Show a successfully saved method
        Alert alert;
        if (saveSuccessful) {
            // Create a success message box
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Save POI");
            alert.setHeaderText("Successfully saved the POI!");
            alert.setContentText("The POI was successfully added to your list of POIs.");
            // Add a checkmark graphic
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("check_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);

        } else {
            // Create an error message box
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Saving POI");
            alert.setHeaderText("Unable to save POI!");
            alert.setContentText("An error occurred while saving.");
            // Add an error graphic
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("error_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);

        }

        // Make the alert prevent interaction with the windows behind and force it on the same screen
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

        alert.showAndWait();
    }

    /**
     * This method is used to edit a POI using the given information and call the required helper methods to edit it
     *
     * @param name        The name of the POI
     * @param roomNumber  The room number for the POI
     * @param description The description for the POI
     * @param actionEvent an ActionEvent object representing the click event
     */
    private void edit(String name, String roomNumber, String description, ActionEvent actionEvent) {
        // Used to store if the user data saved successfully
        boolean saveSuccessful;

        // Get currently selected POI
        PointOfInterest currentSelectedPoi = CurrentUser.getCurrentSelectedPoi();

        // Remove the POI from the favourites before editing
        currentSelectedPoi.setFavorite(false);
        FavoritePoi.setUserFavourite(currentSelectedPoi);

        // Create a new POI to store the updated information
        PointOfInterest editedPOI = new PointOfInterest();

        // Add the information to the edited POI
        editedPOI.setName(name);
        editedPOI.setRoomNumber(roomNumber);
        editedPOI.setDescription(description);
        editedPOI.setFavorite(favourite);
        editedPOI.setX(currentSelectedPoi.getX());
        editedPOI.setY(currentSelectedPoi.getY());
        // Set the type for the POI
        editedPOI.setType("User POI");

        // Call the method to edit the POI in the current user's data object
        CurrentUser.editPoi(editedPOI);
        // Save the object to a JSON file and store if the method saved successfully
        saveSuccessful = CurrentUser.saveUserData();

        // Set the updated POI as a favourite or not
        FavoritePoi.setUserFavourite(editedPOI);

        Alert alert;
        // Show a successfully saved alert
        if (saveSuccessful) {
            // Create a success message box
            alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Updated POI");
            alert.setHeaderText("Successfully updated the POI!");
            alert.setContentText("The POI was successfully updated in your list of POIs.");
            // Add a checkmark graphic
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("check_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);
        } else { // Otherwise, show an error
            // Create an error message box
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Updated POI");
            alert.setHeaderText("Unable to update the POI!");
            alert.setContentText("An error occurred while updating.");
            // Add an error graphic
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("error_icon.png")));
            imageView.setFitHeight(50);
            imageView.setFitWidth(50);
            alert.setGraphic(imageView);

        }

        // Make the alert prevent interaction with the windows behind and force it on the same screen
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(((Node) actionEvent.getSource()).getScene().getWindow());

        alert.showAndWait();
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

    /**
     * This method is used to set the text fields of the popup and the state of favourite
     * based on the selected POI stored in CurrentUser
     */
    private void setFields() {
        // Get the currently selected POI
        PointOfInterest currentSelectedPoi = CurrentUser.getCurrentSelectedPoi();

        // Set the fields
        NameField.setText(currentSelectedPoi.getName());
        RoomNumberField.setText(currentSelectedPoi.getRoomNumber());
        DescriptionField.setText(currentSelectedPoi.getDescription());
        // Set the state of favourite
        favourite = currentSelectedPoi.isFavorite();
    }
}
