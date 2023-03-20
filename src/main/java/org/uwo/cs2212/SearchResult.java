package org.uwo.cs2212;

import org.uwo.cs2212.model.FloorMap;
import org.uwo.cs2212.model.PointOfInterest;

public class SearchResult {
    private PointOfInterest poi;
    private FloorMap floorMap;

    public SearchResult(FloorMap floorMap, PointOfInterest poi){
        this.floorMap = floorMap;
        this.poi = poi;
    }

    public PointOfInterest getPoi() {
        return poi;
    }

    public void setPoi(PointOfInterest poi) {
        this.poi = poi;
    }

    public FloorMap getFloorMap() {
        return floorMap;
    }

    public void setFloorMap(FloorMap floorMap) {
        this.floorMap = floorMap;
    }

    @Override
    public String toString(){
        return "Floor: " + floorMap.getName() + "\n" + poi.toString();
    }
}
