package org.uwo.cs2212.model;

import org.uwo.cs2212.CurrentUser;

/**
 * This class is used to store data on a favourite POI. This includes, the map, floor and layer name, along
 * with the POI name.
 *
 * @author Yaopeng Xie
 */
public class FavoritePoi {
    private String baseMapName;
    private String floorMapName;
    private String layerName;
    private String poiName;

    public FavoritePoi(){
    }

    public FavoritePoi(String baseMapName, String floorMapName, String layerName, String poiName){
        this.baseMapName = baseMapName;
        this.floorMapName = floorMapName;
        this.layerName = layerName;
        this.poiName = poiName;
    }

    public String getBaseMapName() {
        return baseMapName;
    }

    public void setBaseMapName(String baseMapName) {
        this.baseMapName = baseMapName;
    }

    public String getFloorMapName() {
        return floorMapName;
    }

    public void setFloorMapName(String floorMapName) {
        this.floorMapName = floorMapName;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    /**
     * This method is used to set if a POI is a favourite or not for the user
     *
     * @param poi The POI to set as a favourite or not
     */
    public static void setUserFavourite(PointOfInterest poi) {
        // If the POI is a favourite add it to the favourite list
        if (poi.isFavorite()) {
            // If a mapConfig and the layer can be found (none user POIs) add the favourite
            if (CurrentUser.getMapConfig() != null && PointOfInterest.findPOILayer(poi, CurrentUser.getMapConfig()) != null) {
                CurrentUser.getUserData().addFavourite(poi, CurrentUser.getCurrentBaseMap(),
                        PointOfInterest.findPOILayer(poi, CurrentUser.getMapConfig()), CurrentUser.getCurrentFloorMap());
            } else if (PointOfInterest.findUserPOILayer(poi) != null) {
                CurrentUser.getUserData().addFavourite(poi, CurrentUser.getCurrentBaseMap(),
                        PointOfInterest.findUserPOILayer(poi), CurrentUser.getCurrentFloorMap());
            }
        } else { // Otherwise, remove it
            CurrentUser.getUserData().removeFavourite(poi, CurrentUser.getCurrentBaseMap(), CurrentUser.getCurrentFloorMap());
        }
    }
}
