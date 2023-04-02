package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
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
     * This variable is used to store the coordinates of the POI
     */
    private static Point2D poiCoords;
    /**
     * This variable is used to store the stage for the popup view
     */
    private static Stage stage;

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
     * @param coords The coordinates to be saved with the POI
     */
    public static void setCoords(Point2D coords) {
        PoiPopupController.poiCoords = coords;
    }

    /**
     * This method is called when the favourite button is clicked which calls the setFavouriteButtonState
     * method to toggle the favourite state
     * @param actionEvent
     */
    public void OnFavouriteClicked(ActionEvent actionEvent) {
        setFavouriteButtonState();
    }

    /**
     * This method is called when the save button is clicked this collects all the data the user
     * entered then runs the save method
     * @param actionEvent
     */
    public void OnSaveClicked(ActionEvent actionEvent) {

        if (NameField.getText().equals("") || RoomNumberField.getText().equals("")) {
            System.out.println("Error you can't continue because you need a name and room number");
        } else {
            System.out.println(NameField.getText());
            System.out.println(RoomNumberField.getText());
            System.out.println(DescriptionField.getText());
            System.out.println(favourite);
            System.out.println("x = " + poiCoords.getX());
            System.out.println("y = " + poiCoords.getY());

            save(NameField.getText(), RoomNumberField.getText(), DescriptionField.getText());

            stage.close();
        }
    }

    private void save(String name, String roomNumber, String description) {
        PointOfInterest poi = new PointOfInterest();

        poi.setName(name);
        poi.setRoomNumber(roomNumber);
        poi.setDescription(description);
        poi.setFavorite(favourite);
        poi.setX(poiCoords.getX());
        poi.setY(poiCoords.getY());

        poi.setType("User");

        ObjectMapper mapper = new ObjectMapper();

//        try {

            //FileWriter fileWriter = new FileWriter("./src/main/resources/org/uwo/cs2212/" + CurrentUser.getUsername().getPoiFileName());

            // Writing to a file
            //mapper.writeValue(fileWriter, poi );

            System.out.println(poi);

//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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
