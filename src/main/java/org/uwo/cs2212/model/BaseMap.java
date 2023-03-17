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
}
