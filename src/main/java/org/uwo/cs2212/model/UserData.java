package org.uwo.cs2212.model;

import org.uwo.cs2212.CurrentUser;
import org.uwo.cs2212.SearchResult;

import java.util.ArrayList;
import java.util.List;

public class UserData {
    private List<UserLayer> userLayers;
    private List<FavoritePoi> favoritePois;
    public UserData() {
        this.userLayers = new ArrayList<>();
        this.favoritePois = new ArrayList<>();
    }
    public List<UserLayer> getUserLayers() {
        return userLayers;
    }

    public void setUserLayers(List<UserLayer> userLayers) {
        this.userLayers = userLayers;
    }

    public void addPoi(BaseMap baseMap, FloorMap floorMap, PointOfInterest poi){
        // Used to store if a new userLayer had to be created
        boolean newUserLayerCreated = false;

        UserLayer userLayer = findUserLayer(baseMap, floorMap, this);
        if (userLayer == null){
            newUserLayerCreated = true;
            userLayer = new UserLayer();
            userLayer.setBaseName(baseMap.getName());
            userLayer.setFloorName(floorMap.getName());
            userLayer.setName("User layer");
            userLayer.setHideLayer(false);
            userLayer.setColor("BLACK");
            userLayer.setFont("Arial");
            userLayer.setLayerType("User layer"); // TODO: Correct?
            userLayer.setSize(16);
            userLayer.setPoints(new ArrayList<>());
        }

        // Add the poi to the userLayer
        userLayer.getPoints().add(poi);

        // If userLayers is empty create a new list
        if(userLayers == null){
            userLayers = new ArrayList<>();
        }

        // If a new user layer was created then add it to the userLayers
        if (newUserLayerCreated) {
            userLayers.add(userLayer);
        } else { // Otherwise, update the existing layer
            updateUserLayers(userLayer);
        }
    }

    /**
     * This method is used to update the UserLayer in the list for the corresponding Map and floor layer
     *
     * @param updatedUserLayer The layer that will replace the older UserLayer in the list
     */
    public void updateUserLayers(UserLayer updatedUserLayer) {
        // Loop through all UserLayers in the list
        for (UserLayer userLayer : userLayers) {
            // If the userLayer equals the updated one then get the index of current userLayer in the list,
            // and call the set method to replace the old userLayer with the updated one
            if (userLayer.equals(updatedUserLayer)) {
                int index = userLayers.indexOf(userLayer);
                userLayers.set(index, updatedUserLayer);
                break; // Break early since the layer was found
            }
        }
    }

    /**
     * This method is used to remove a favourite POI from the favoritePois list
     *
     * @param poiName The name of the favourite POI to be removed
     * @param baseMapName The name of the matching baseMap for POI to be removed
     * @param floorMapName The name of the matching floorMap for POI to be removed
     */
    public void removeFavourite(PointOfInterest poi, String poiName, String baseMapName, String floorMapName) {
        // Loop through the list of the favouritePois
        for (FavoritePoi favPoi : favoritePois) {
            // If favPoi matches the given names then remove the favourite at the index
            if (favPoi.getPoiName().equals(poiName) && favPoi.getBaseMapName().equals(baseMapName) &&
                    favPoi.getFloorMapName().equals(floorMapName)) {
                int index = favoritePois.indexOf(favPoi);
                favoritePois.remove(index);
                break; // Break early since the layer was found
            }
        }

        // Modify the favourite value in the UserLayer list
        swapFavouriteVal(poi);

        // Save the updated object to the json file
        CurrentUser.saveUserData();
    }

    /**
     * This method is used to swap the favourite value from its current value, so if it is a favourite then it will
     * be not a favourite and vise-versa.
     *
     * @param searchPoi The poi to match to change the favourite status of
     */
    private void swapFavouriteVal(PointOfInterest searchPoi) {
        // Loop through all userLayers and POIs
        for (UserLayer userLayer : userLayers) {
            for (PointOfInterest poi : userLayer.getPoints()) {
                // If the POI is found change swap it's boolean value
                if (poi.equals(searchPoi)) {
                    poi.setFavorite(!poi.isFavorite());
                }
            }
        }
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
