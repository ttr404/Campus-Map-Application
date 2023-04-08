package org.uwo.cs2212;

import org.uwo.cs2212.model.*;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to store data on the logged-in user. This includes their user generated POIs and their favourites
 *
 * @author Yaopeng Xie
 * @author Jarrett Boersen
 */
public class CurrentUser {
    private static String username;
    private static PointOfInterest currentSelectedPoi;
    private static MapConfig mapConfig;
    private static BaseMap currentBaseMap;
    private static FloorMap currentFloorMap;
    private static UserData userData;

    public static boolean isAdmin(){
        return username != null && username.equalsIgnoreCase("admin");
    }

    public static void setUsername(String username) {
        CurrentUser.username = username;
    }

    public static String getUsername() {
        return CurrentUser.username;
    }

    public static PointOfInterest getCurrentSelectedPoi() {
        return currentSelectedPoi;
    }

    public static void setCurrentSelectedPoi(PointOfInterest currentSelectedPoi) {
        CurrentUser.currentSelectedPoi = currentSelectedPoi;
    }

    public static MapConfig getMapConfig() {
        return mapConfig;
    }

    public static void setMapConfig(MapConfig mapConfig) {
        CurrentUser.mapConfig = mapConfig;
    }

    public static BaseMap getCurrentBaseMap() {
        return currentBaseMap;
    }

    public static void setCurrentBaseMap(BaseMap currentBaseMap) {
        CurrentUser.currentBaseMap = currentBaseMap;
    }

    public static FloorMap getCurrentFloorMap() {
        return currentFloorMap;
    }

    public static void setCurrentFloorMap(FloorMap currentFloorMap) {
        CurrentUser.currentFloorMap = currentFloorMap;
    }

    public static UserData getUserData() {
        return userData;
    }

    public static void setUserData(UserData userData) {
        CurrentUser.userData = userData;
    }

    /**
     * This function allow the application to get the user's json file url
     *
     * @return the url of current user's json file
     */
    public static URL getCurrentUserLayerUrl(){
        return CurrentUser.class.getResource("user-layer-" + username.trim().toLowerCase() + ".json");
    }

    /**
     * This method is used to add the POI data to the user layer
     *
     * @param baseMap the base map of the user layer
     * @param floorMap the floor map of the user layer
     * @param poi the poi will be added into the user layer
     */
    public static void addPoi(BaseMap baseMap, FloorMap floorMap, PointOfInterest poi){
        if (userData == null){
            userData = new UserData();
        }
        if (baseMap == null || floorMap == null || poi == null){
            return;
        }
        userData.addPoi(baseMap, floorMap, poi);
    }

    /**
     * This method is used to remove the selected POI from the user layer
     */
    public static void removeSelectedPOI() {
        BaseMap baseMap = CurrentUser.getCurrentBaseMap();
        FloorMap floorMap = CurrentUser.getCurrentFloorMap();
        PointOfInterest poi = CurrentUser.getCurrentSelectedPoi();

        if (baseMap == null || floorMap == null || poi == null){
            return;
        }

        userData.removePOI(baseMap, floorMap, poi);
    }

    /**
     * This method is used to update selected POI in the user layer
     *
     * @param updatedPOI The updated version of the POI (this is a new POI instead of editing the old one to allow
     *                   searching for the old POI)
     */
    public static void editPoi(PointOfInterest updatedPOI){
        BaseMap baseMap = CurrentUser.getCurrentBaseMap();
        FloorMap floorMap = CurrentUser.getCurrentFloorMap();
        PointOfInterest poi = CurrentUser.getCurrentSelectedPoi();

        if (baseMap == null || floorMap == null || poi == null){
            return;
        }

        userData.editPOI(baseMap, floorMap, poi, updatedPOI);
    }

    /**
     * Saves the user data to a file, by calling the saveUserData method from the ConfigUtil class. The user data includes the list
     * of favorite points of interest, which are extracted from the mapConfig object. The method also creates a new user layer file,
     * if one does not already exist, and updates the current user layer URL accordingly. The method returns a boolean value indicating
     * whether the user data was saved successfully or not.
     *
     * @return true if the user data was saved successfully, false otherwise
     */
    public static boolean saveUserData() {
        // Used to store if the user data saved successfully
        boolean saveSuccessful = true;

        URL tmpUrl = CurrentUser.getCurrentUserLayerUrl();
        if (tmpUrl == null) {
            tmpUrl = CurrentUser.class.getResource("empty-user-layer.json"); // TODO: Switch this from the target dir to the resource one?
            String path = tmpUrl.getPath();
            path = path.replace("empty-user-layer.json", "user-layer-" + CurrentUser.getUsername().trim().toLowerCase() + ".json");
            path = path.replace("%20", " ");
            try {
                File file = new File(path);
                System.out.println(path);
                file.createNewFile();
                tmpUrl = CurrentUser.getCurrentUserLayerUrl();
            } catch (MalformedURLException e) {
                saveSuccessful = false;
                e.printStackTrace();
            } catch (IOException e) {
                saveSuccessful = false;
                e.printStackTrace();
            }
        }

        ConfigUtil.saveUserData(userData, tmpUrl);

        // Returns true if no errors occurred
        return saveSuccessful;
    }

    /**
     * This method is used to reset thr user's session data (the userData list containing user created POIs and favourites)
     */
    public static void resetSessionData() {
        userData = new UserData();
    }
}
