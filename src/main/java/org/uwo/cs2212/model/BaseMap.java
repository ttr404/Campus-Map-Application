package org.uwo.cs2212.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.uwo.cs2212.CurrentUser;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store the base map, which is a building or the campus map. This includes its name,
 * and list of floor configs and maps.
 *
 * @author Yaopeng Xie
 */
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
     * Check if the 2 BaseMap are equal to each other
     *
     * @param other The other BaseMap to compare to
     * @return Returns true if they match, otherwise, it returns false
     */
    public boolean equals(BaseMap other) {
        return this.getName().equals(other.getName());
    }

    /**
     * This method is used to find a BaseMap given a FloorMap
     *
     * @param searchFloorMap The FloorMap that will match to a BaseMap
     * @return Returns the matching BaseMap if it exits, otherwise, it returns null
     */
    public static BaseMap findBaseMap(FloorMap searchFloorMap) {
        // Look for a matching FloorMap
        for (BaseMap baseMap : CurrentUser.getMapConfig().getBaseMaps()) {
            for (FloorMap floorMap : baseMap.floorMaps) {
                if (floorMap.equals(searchFloorMap)) {
                    return baseMap;
                }
            }
        }

        return null;
    }
}
