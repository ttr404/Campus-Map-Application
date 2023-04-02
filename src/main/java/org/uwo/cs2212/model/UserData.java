package org.uwo.cs2212.model;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private List<UserLayer> userLayers;
    private List<FavoritePoi> favoritePois;

    public List<UserLayer> getUserLayers() {
        return userLayers;
    }

    public void setUserLayers(List<UserLayer> userLayers) {
        this.userLayers = userLayers;
    }

    public void addPoi(BaseMap baseMap, FloorMap floorMap, PointOfInterest poi){
        UserLayer userLayer = findUserLayer(baseMap, floorMap, this);
        if (userLayer == null){
            userLayer = new UserLayer();
            userLayer.setBaseName(baseMap.getName());
            userLayer.setFloorName(floorMap.getName());
            userLayer.setName("User layer");
            userLayer.setHideLayer(false);
            userLayer.setColor("BLACK");
            userLayer.setFont("Arial");
            userLayer.setLayerType("internal");
            userLayer.setSize(16);
            userLayer.setPoints(new ArrayList<>());
        }
        userLayer.getPoints().add(poi);
        if(userLayers == null){
            userLayers = new ArrayList<>();
        }
        userLayers.add(userLayer);
    }

    public static UserLayer findUserLayer(BaseMap baseMap, FloorMap floorMap, UserData userLayerList){
        if (userLayerList != null && userLayerList.getUserLayers() != null){
            for(UserLayer userLayer : userLayerList.getUserLayers()){
                if(userLayer.getFloorName().equals(floorMap.getName()) && userLayer.getBaseName().equals(baseMap.getName())){
                    return userLayer;
                }
            }
        }
        return null;
    }

    public List<FavoritePoi> getFavoritePois() {
        return favoritePois;
    }

    public void setFavoritePois(List<FavoritePoi> favoritePois) {
        this.favoritePois = favoritePois;
    }
}
