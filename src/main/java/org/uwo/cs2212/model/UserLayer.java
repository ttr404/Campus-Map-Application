package org.uwo.cs2212.model;

public class UserLayer extends Layer {
    private String floorName;
    private String baseName;

    public String getFloorName() {
        return floorName;
    }

    public void setFloorName(String floorName) {
        this.floorName = floorName;
    }

    public String getBaseName() {
        return baseName;
    }

    public void setBaseName(String baseName) {
        this.baseName = baseName;
    }

    /**
     * This method is used to check if 2 UserLayers are equal to each other
     *
     * @param other The other UserLayer to be checked
     * @return Returns true if they are equal, otherwise, it returns false
     */
    public boolean equals(UserLayer other) {
        // Add null check for the 'other' variable
        if (other == null){
            return false;
        }
        return this.floorName.equals(other.floorName) && this.baseName.equals(other.baseName);
    }
}
