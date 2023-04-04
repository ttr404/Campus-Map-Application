package org.uwo.cs2212.model;

import org.uwo.cs2212.CurrentUser;
import org.uwo.cs2212.SearchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents user-specific data, such as user-created layers and favorite Points of Interest (POIs).
 */
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


    /**
     * Adds a Point of Interest (POI) to the corresponding UserLayer for the given BaseMap and FloorMap.
     *
     * @param baseMap the BaseMap containing the FloorMap
     * @param floorMap the FloorMap where the POI is located
     * @param poi the Point of Interest to be added
     */
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
     * Updates a UserLayer in the list with a new version.
     *
     * @param updatedUserLayer the updated UserLayer to replace the old one in the list
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
     * Removes a favorite POI from the favoritePois list.
     *
     * @param poi the POI to remove from the favoritePois list
     * @param poiName the name of the POI to be removed
     * @param baseMapName the name of the corresponding BaseMap for the POI to be removed
     * @param floorMapName the name of the corresponding FloorMap for the POI to be removed
     */
    public void removeFavourite(PointOfInterest poi, String poiName, String baseMapName, String floorMapName) {
        // Loop through the list of the favouritePois
        for (FavoritePoi favPoi : favoritePois) {
            // If favPoi matches the given names then remove the favourite at the index
            if (favPoi.getPoiName().equals(poiName) && favPoi.getBaseMapName().equals(baseMapName) &&
                    favPoi.getFloorMapName().equals(floorMapName)) {
                favoritePois.remove(favPoi);
                break; // Break early since the layer was found
            }
        }

        // Modify the favourite value in the UserLayer list
        swapFavouriteVal(poi);

        // Save the updated object to the json file
        CurrentUser.saveUserData();
    }

    /**
     * Swaps the favorite status of the specified POI.
     * If the POI is a favorite, it will be set as not a favorite and vice versa.
     *
     * @param searchPoi the POI to change the favorite status of
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
