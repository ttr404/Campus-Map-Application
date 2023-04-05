package org.uwo.cs2212;

import org.uwo.cs2212.model.FloorMap;
import org.uwo.cs2212.model.PointOfInterest;

/**
 * The SearchResult class represents a search result containing a PointOfInterest object
 * and the FloorMap object where the point of interest is located. It provides methods
 * for getting and setting the PointOfInterest and FloorMap objects associated with the
 * search result, as well as for generating a String representation of the search result.
 *
 * @author Yaopeng Xie
 * @author Tingrui Zhang
 */
public class SearchResult {
    private PointOfInterest poi;
    private FloorMap floorMap;


    /**
     * Creates a new SearchResult object representing a point of interest on a specific floor map.
     *
     * @param floorMap the FloorMap object where the point of interest is located
     * @param poi the PointOfInterest object representing the point of interest
     */
    public SearchResult(FloorMap floorMap, PointOfInterest poi){
        this.floorMap = floorMap;
        this.poi = poi;
    }

    /**
     * Returns the PointOfInterest object associated with this search result.
     *
     * @return the PointOfInterest object
     */
    public PointOfInterest getPoi() {
        return poi;
    }

    /**
     * Sets the PointOfInterest object associated with this search result.
     *
     * @param poi the PointOfInterest object to set
     */
    public void setPoi(PointOfInterest poi) {
        this.poi = poi;
    }

    /**
     * Returns the FloorMap object associated with this search result.
     *
     * @return the FloorMap object
     */
    public FloorMap getFloorMap() {
        return floorMap;
    }

    /**
     * Sets the FloorMap object associated with this search result.
     *
     * @param floorMap the FloorMap object to set
     */
    public void setFloorMap(FloorMap floorMap) {
        this.floorMap = floorMap;
    }

    /**
     * Returns a String representation of this search result in the format "Floor: [floor map name]\n[point of interest information]".
     *
     * @return a String representation of this search result
     */
    @Override
    public String toString(){
        // Handling Null cases
        String floorMapName = floorMap != null ? floorMap.getName() : "null";
        String poiName = poi != null ? poi.toString() : "null";

        return "Floor: " + floorMapName + "\n" + poiName;
    }

}
