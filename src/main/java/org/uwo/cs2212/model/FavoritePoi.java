package org.uwo.cs2212.model;

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

}
