package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.uwo.cs2212.model.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The ConfigUtil class provides utility methods for loading and saving
 * configuration objects such as MapConfig, FloorMap, and UserList to
 * and from JSON files. It makes use of the Jackson library for JSON
 * deserialization and serialization.
 *
 * @author Yaopeng Xie
 * @author Tingrui Zhang
 * @author Jarrett Boersen
 */
public class ConfigUtil {
    /**
     * Loads a MapConfig object from the specified JSON file.
     * This method reads the JSON file from the given URL, parses it,
     * and maps the JSON data to a MapConfig object. It also loads
     * the FloorMap objects for each BaseMap object present in the
     * MapConfig object by iterating through the BaseMap and FloorMap
     * configurations.
     *
     * @param fileName The file name of the JSON file containing the map configuration
     *            data to be loaded.
     * @return The MapConfig object containing the map configuration data
     * from the JSON file, including the associated FloorMap objects.
     * @throws RuntimeException If there is an IOException or
     *                          URISyntaxException while reading the JSON file, mapping the
     *                          JSON data to the MapConfig object, or loading the FloorMap
     *                          objects.
     */
    public static MapConfig loadMapConfig(String fileName) {
        try {
            // Read the JSON file data to a byte array
            byte[] mapJsonData = Files.readAllBytes(getCorrectPath(fileName));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a MapConfig object
            MapConfig mapConfig = objectMapper.readValue(mapJsonData, MapConfig.class);

            File file = new File(CurrentUser.getCurrentUserLayerPath());
            // Check if the file contains data
            if (file.length() > 0) {
                CurrentUser.setUserData(ConfigUtil.loadUserLayers(file.getPath()));
            } else {
                // Delete the file if it is blank to prevent crashes
                try {
                    if (file.delete()) {
                        System.out.println(file.getName() + " is deleted!");
                    } else {
                        System.out.println("Delete operation is failed.");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            // Iterate through the BaseMap objects in the MapConfig object
            for (BaseMap baseMap : mapConfig.getBaseMaps()) {

                // Iterate through the FloorMap configurations in each BaseMap object
                for (FloorMap floorConfig : baseMap.getFloorConfigs()) {

                    // Get the URL of the FloorMap configuration file
                    String floorMapUrl = ConfigUtil.getCorrectPath(floorConfig.getConfigFileName()).toString();


                    // Load the FloorMap object using the configuration file URL
                    FloorMap floorMap = loadFloorMap(floorMapUrl);
                    floorMap.setConfigFileName(floorConfig.getConfigFileName());

                    UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, CurrentUser.getUserData());
                    if (userLayer != null) {
                        floorMap.setUserLayer(userLayer);
                    }

                    if (CurrentUser.getUserData() != null && CurrentUser.getUserData().getFavoritePois() != null) {
                        updateFavorite(baseMap, floorMap);
                    }


                    // Add the loaded FloorMap object to the BaseMap object
                    baseMap.getFloorMaps().add(floorMap);
                }
            }
            return mapConfig;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a FloorMap object from the specified JSON file.
     * This method reads the JSON file from the given URL, parses it, and maps the JSON data to a FloorMap object.
     *
     * @param fileName The file name of the JSON file containing the floor map
     *            configuration data to be loaded.
     * @return The FloorMap object containing the floor map configuration
     * data from the JSON file.
     * @throws RuntimeException If there is an IOException or
     *                          URISyntaxException while reading the JSON file or mapping
     *                          the JSON data to the FloorMap object.
     */
    public static FloorMap loadFloorMap(String fileName) {
        try {
            // Read the JSON file data to a byte array
            byte[] mapJsonData = Files.readAllBytes(Path.of(fileName));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a FloorMap object
            FloorMap floorMap = objectMapper.readValue(mapJsonData, FloorMap.class);

            // Return the populated FloorMap object
            return floorMap;
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a UserList object from the specified JSON file.
     * This method reads the JSON file from the given URL, parses it,and maps the JSON data to a UserList object.
     *
     * @param fileName The file name of the JSON file containing the user data to be loaded.
     * @return The UserList object containing the user data from the JSON file.
     * @throws RuntimeException If there is an IOException or URISyntaxException while reading the JSON file or
     *                          mapping the JSON data to the UserList object.
     */
    public static UserList loadUserList(String fileName) {
        try {
            // Read the JSON file data to a byte array
            byte[] userJsonData = Files.readAllBytes(getCorrectPath(fileName));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a UserList object
            UserList userList = objectMapper.readValue(userJsonData, UserList.class);

            // Return the populated UserList object
            return userList;
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }


    /**
     * Loads the user layers from a JSON file at the specified URL and returns a UserData object containing the user layers.
     *
     * @param fileName the file name of the JSON file containing the user layers data
     * @return UserData object containing the user layers
     * @throws RuntimeException if an IOException or URISyntaxException occurs during the load operation
     */
    public static UserData loadUserLayers(String fileName) {
        try {
            // Read the JSON file data to a byte array
            byte[] mapJsonData = Files.readAllBytes(Path.of(fileName));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a userLayerList object
            UserData userLayers = objectMapper.readValue(mapJsonData, UserData.class);

            // Return the populated userLayerList object
            return userLayers;
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the given UserData object to the specified file URL as a JSON string.
     *
     * @param userData the UserData object to be saved
     * @param fileName      the file name of the file where the UserData object will be saved
     * @throws RuntimeException if an IOException or URISyntaxException occurs during the save operation
     */
    public static void saveUserData(UserData userData, String fileName) {
        try {
            // Convert the URL to a Path object representing the file path
            Path filePath = Path.of(getUserSaveFolder() + "/" + fileName);

            // Create an ObjectMapper instance for handling JSON serialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Enable pretty-printing (indentation) of the JSON output
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Convert the userData object to a JSON string
            String userJsonData = objectMapper.writeValueAsString(userData);

            // Write the JSON string back to the file
            Files.writeString(filePath, userJsonData);
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Saves the provided UserList object to the specified JSON file.
     * This method serializes the UserList object into a JSON string
     * and writes it to the file at the given URL.
     *
     * @param userList The UserList object containing the user data
     *                 to be saved.
     * @param fileName      The file name of the JSON file where the user data should
     *                 be saved.
     * @throws RuntimeException If there is an IOException or
     *                          URISyntaxException while writing the JSON data to the file.
     */
    public static void saveUserList(UserList userList, String fileName) {
        try {
            // Convert the URL to a Path object representing the file path
            Path filePath = Path.of(getUserSaveFolder() + "/" + fileName);

            // Create an ObjectMapper instance for handling JSON serialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Enable pretty-printing (indentation) of the JSON output
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

            // Convert the UserList object to a JSON string
            String userJsonData = objectMapper.writeValueAsString(userList);

            // Write the JSON string back to the file
            Files.writeString(filePath, userJsonData);
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Updates the favorite status of Points of Interest (POIs) in the given BaseMap and FloorMap
     * based on the current user's list of favorite POIs.
     *
     * @param baseMap  the BaseMap containing the FloorMap
     * @param floorMap the FloorMap containing the layers with POIs to update
     */
    private static void updateFavorite(BaseMap baseMap, FloorMap floorMap) {
        // Loop through all the layers in the floorMap
        for (Layer layer : floorMap.getLayers()) {
            // Loop through all the POIs in the layer
            for (PointOfInterest poi : layer.getPoints()) {
                // Loop through all the favorite POIs of the current user
                for (FavoritePoi favorite : CurrentUser.getUserData().getFavoritePois()) {
                    // If the favorite POI matches the baseMap, floorMap, layer, and POI names, set the POI as a favorite
                    if (favorite.getBaseMapName().equalsIgnoreCase(baseMap.getName())
                            && favorite.getFloorMapName().equalsIgnoreCase(floorMap.getName())
                            && favorite.getLayerName().equalsIgnoreCase(layer.getName())
                            && favorite.getPoiName().equalsIgnoreCase(poi.getName())) {
                        poi.setFavorite(true);
                    }
                }
            }
        }

        // If the floorMap has a userLayer with POIs, loop through the user-created POIs
        if (floorMap.getUserLayer() != null && floorMap.getUserLayer().getPoints() != null) {
            for (PointOfInterest poi : floorMap.getUserLayer().getPoints()) {
                // Loop through all the favorite POIs of the current user
                for (FavoritePoi favorite : CurrentUser.getUserData().getFavoritePois()) {
                    // If the favorite POI matches the baseMap, floorMap, userLayer, and POI names, set the POI as a favorite
                    if (favorite.getBaseMapName().equalsIgnoreCase(baseMap.getName())
                            && favorite.getFloorMapName().equalsIgnoreCase(floorMap.getName())
                            && favorite.getLayerName().equalsIgnoreCase(floorMap.getUserLayer().getName())
                            && favorite.getPoiName().equalsIgnoreCase(poi.getName())) {
                        poi.setFavorite(true);
                    }
                }
            }
        }
    }

    private static Class resourceClass;
    public static void resourceClass(Class resourceClass) {
        ConfigUtil.resourceClass = resourceClass;
    }

    public static Path getCorrectPath(String fileName) {
        // Get the location of the file in the project's resource folder
        InputStream resourceUrl = CurrentUser.class.getResourceAsStream(fileName);

//        String resourcePath = resourceUrl.;
//        resourcePath = resourcePath.replace("map-config.json", fileName);
//        resourcePath = resourcePath.replace("%20", " ");

        File resourceFile = new File(resourceUrl);

        // Check if the file exists in the user's home folder
        File userFile = new File(getUserSaveFolder() + "/" + fileName);
        if (userFile.exists()) {
            return userFile.toPath();
        } else {
            return resourceFile.toPath();
        }
    }

    public static String getUserSaveFolder() {
        // Get the user's home directory
        String userUrlString = System.getProperty("user.home");
        userUrlString = userUrlString.replace("%20", " ");

        // Create the storage folder
        File userDir = new File(userUrlString + "/Western Campus Map");
        if (!userDir.exists()){
            userDir.mkdirs();
        }

        return userDir.getPath();
    }
}
