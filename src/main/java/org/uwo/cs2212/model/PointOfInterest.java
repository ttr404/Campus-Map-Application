package org.uwo.cs2212.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.uwo.cs2212.CurrentUser;

/**
 * This class is used to store data on the Point of Interest. This includes, the coordinates, name, room number,
 * description, type, if it is selected or a favourite
 *
 * @author Yaopeng Xie
 * @author Jarrett Boersen
 * @author Tingrui Zhang
 */
public class PointOfInterest {
    private double x;
    private double y;
    private String name;
    private String roomNumber;
    private String description;
    private String type;
    @JsonIgnore
    private boolean selected;
    @JsonIgnore
    private boolean favourite;

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isFavourite() {
        return favourite;
    }

    public void setFavourite(boolean favourite) {
        this.favourite = favourite;
    }

    @Override
    public String toString() {
        return "Room Type: " + type + "\n" + "Room Name: " + name + " \n" + description;
    }

    /**
     * This method is used to check if 2 PointOfInterests are equal to each other
     *
     * @param other The other PointOfInterest to be checked
     * @return Returns true if they are equal, otherwise, it returns false
     */
    public boolean equals(PointOfInterest other) {
        // Add null check for the 'type' variable
        if (type == null) {
            return other.type == null;
        }

        return type.equals(other.type) && name.equals(other.name) && roomNumber.equals(other.roomNumber) && description.equals(other.description);
    }

    /**
     * This method is used to match a layer to a POI
     *
     * @param searchPoi The POI to match the layer with
     * @param mapConfig The MapConfig that stores all the maps
     * @return Returns the layer if it was found to match, otherwise, it returns null
     */
    public static Layer findPOILayer(PointOfInterest searchPoi, MapConfig mapConfig) {
        for (BaseMap baseMap : mapConfig.getBaseMaps()) {
            for (FloorMap floorMap : baseMap.getFloorMaps()) {
                for (Layer layer : floorMap.getLayers()) {
                    for (PointOfInterest poi : layer.getPoints()) {
                        // If the POIs match return the layer
                        if (poi.equals(searchPoi)) {
                            return layer;
                        }
                    }
                }
            }
        }

        return null; // No matching layer found
    }

    /**
     * This method is used to match a user layer to a POI
     *
     * @param searchPoi The POI to match the layer with
     * @return Returns the layer if it was found to match, otherwise, it returns null
     */
    public static Layer findUserPOILayer(PointOfInterest searchPoi) {
        for (Layer layer : CurrentUser.getUserData().getUserLayers()) {
            for (PointOfInterest poi : layer.getPoints()) {
                // If the POIs match return the layer
                if (poi.equals(searchPoi)) {
                    return layer;
                }
            }
        }

        return null; // No matching layer found
    }
}
