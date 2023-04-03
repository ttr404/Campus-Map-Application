package org.uwo.cs2212.model;

import com.fasterxml.jackson.annotation.JsonIgnore;


public class PointOfInterest {
    private double x;
    private double y;
    private String name;
    private String roomNumber;
    private String description;
    private String type;
    @JsonIgnore
    private boolean selected;
    // TODO: Keep ignore removed?
    private boolean favorite;


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

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }
    @Override
    public String toString(){
        return type + ":" + name + " \n" + description;
    }

    /**
     * This method is used to check if 2 PointOfInterests are equal to each other
     *
     * @param other The other PointOfInterest to be checked
     * @return Returns true if they are equal, otherwise, it returns false
     */
    public boolean equals(PointOfInterest other) {
        return type.equals(other.type) && name.equals(other.name) && roomNumber.equals(other.roomNumber) && description.equals(other.description);
    }
}
