package org.uwo.cs2212.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class BaseMap {
    private String name;
    private List<FloorMap> floorConfigs;
    @JsonIgnore
    private List<FloorMap> floorMaps = new ArrayList<>();


    public List<FloorMap> getFloorConfigs() {
        return floorConfigs;
    }

    public void setFloorConfigs(List<FloorMap> floorConfigs) {
        this.floorConfigs = floorConfigs;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FloorMap> getFloorMaps() {
        return floorMaps;
    }

    public void setFloorMaps(List<FloorMap> floorMaps) {
        this.floorMaps = floorMaps;
    }

    /**
     * This method is used to return a FloorMap given its file name
     *
     * @param fileName The file name of the FloorMap
     * @return Returns the FloorMap object corresponding to the file name, otherwise if it is not found it returns null
     */
    public FloorMap getFloorMapObj(String fileName) {
        // Loop through all the floorMaps
        for (FloorMap floorMap : floorMaps) {
            // If the floorMap's name is equal to the filename then return the floorMap
            if (floorMap.getMapFileName().equals(fileName)) {
                return floorMap;
            }
        }
        return null; // No floor found
    }
}
