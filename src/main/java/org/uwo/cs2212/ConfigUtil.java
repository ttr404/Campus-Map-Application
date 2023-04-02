package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.uwo.cs2212.model.*;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * The ConfigUtil class provides utility methods for loading and saving
 * configuration objects such as MapConfig, FloorMap, and UserList from
 * and to JSON files. It makes use of the Jackson library for JSON
 * deserialization and serialization.
 *
 * @author
 * @author
 * @author
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
     * @param url The URL of the JSON file containing the map configuration
     *            data to be loaded.
     * @return The MapConfig object containing the map configuration data
     *         from the JSON file, including the associated FloorMap objects.
     * @throws RuntimeException If there is an IOException or
     *         URISyntaxException while reading the JSON file, mapping the
     *         JSON data to the MapConfig object, or loading the FloorMap
     *         objects.
     */
    public static MapConfig loadMapConfig(URL url) {
        try {
            // Read the JSON file data to a byte array
            byte[] mapJsonData = Files.readAllBytes(Path.of(url.toURI()));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a MapConfig object
            MapConfig mapConfig = objectMapper.readValue(mapJsonData, MapConfig.class);

            URL currentUserUrl = CurrentUser.getCurrentUserLayerUrl();
            if(currentUserUrl != null){
                CurrentUser.setUserData(ConfigUtil.loadUserLayers(CurrentUser.getCurrentUserLayerUrl()));
            }

            // Iterate through the BaseMap objects in the MapConfig object
            for (BaseMap baseMap : mapConfig.getBaseMaps()) {

                // Iterate through the FloorMap configurations in each BaseMap object
                for (FloorMap floorConfig : baseMap.getFloorConfigs()) {

                    // Get the URL of the FloorMap configuration file
                    URL floorMapUrl = ConfigUtil.class.getResource(floorConfig.getConfigFileName());

                    // Load the FloorMap object using the configuration file URL
                    FloorMap floorMap = loadFloorMap(floorMapUrl);
                    floorMap.setConfigFileName(floorConfig.getConfigFileName());

                    UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, CurrentUser.getUserData());
                    if(userLayer != null){
                        floorMap.setUserLayer(userLayer);
                    }
                    

                    if (CurrentUser.getUserData() != null && CurrentUser.getUserData().getFavoritePois() != null){
                        updateFavorite(baseMap, floorMap);
                    }


                    // Add the loaded FloorMap object to the BaseMap object
                    baseMap.getFloorMaps().add(floorMap);
                }
            }
            return mapConfig;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a FloorMap object from the specified JSON file.
     * This method reads the JSON file from the given URL, parses it,
     * and maps the JSON data to a FloorMap object.
     *
     * @param url The URL of the JSON file containing the floor map
     *            configuration data to be loaded.
     * @return The FloorMap object containing the floor map configuration
     *         data from the JSON file.
     * @throws RuntimeException If there is an IOException or
     *         URISyntaxException while reading the JSON file or mapping
     *         the JSON data to the FloorMap object.
     */
    public static FloorMap loadFloorMap(URL url) {
        try {
            // Read the JSON file data to a byte array
            byte[] mapJsonData = Files.readAllBytes(Path.of(url.toURI()));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a FloorMap object
            FloorMap floorMap = objectMapper.readValue(mapJsonData, FloorMap.class);

            // Return the populated FloorMap object
            return floorMap;
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            // Handle URISyntaxException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    /**
     * Loads a UserList object from the specified JSON file.
     * This method reads the JSON file from the given URL, parses it,
     * and maps the JSON data to a UserList object.
     *
     * @param url The URL of the JSON file containing the user data
     *            to be loaded.
     * @return The UserList object containing the user data from the
     *         JSON file.
     * @throws RuntimeException If there is an IOException or
     *         URISyntaxException while reading the JSON file or
     *         mapping the JSON data to the UserList object.
     */
    public static UserList loadUserList(URL url) {
        try {
            // Read the JSON file data to a byte array
            byte[] userJsonData = Files.readAllBytes(Path.of(url.toURI()));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a UserList object
            UserList userList = objectMapper.readValue(userJsonData, UserList.class);

            // Return the populated UserList object
            return userList;
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            // Handle URISyntaxException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    public static UserData loadUserLayers(URL url) {
        try {
            // Read the JSON file data to a byte array
            byte[] mapJsonData = Files.readAllBytes(Path.of(url.toURI()));

            // Create an ObjectMapper instance for handling JSON deserialization
            ObjectMapper objectMapper = new ObjectMapper();

            // Map the JSON data to a userLayerList object
            UserData userLayers = objectMapper.readValue(mapJsonData, UserData.class);

            // Return the populated userLayerList object
            return userLayers;
        } catch (IOException e) {
            // Handle IOException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            // Handle URISyntaxException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    public static void saveUserData(UserData userData, URL url) {
        try {
            // Convert the URL to a Path object representing the file path
            Path filePath = Path.of(url.toURI());

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
        } catch (URISyntaxException e) {
            // Handle URISyntaxException and rethrow as a RuntimeException
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
     * @param url The URL of the JSON file where the user data should
     *            be saved.
     * @throws RuntimeException If there is an IOException or
     *         URISyntaxException while writing the JSON data to the file.
     */
    public static void saveUserList(UserList userList, URL url) {
        try {
            // Convert the URL to a Path object representing the file path
            Path filePath = Path.of(url.toURI());

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
        } catch (URISyntaxException e) {
            // Handle URISyntaxException and rethrow as a RuntimeException
            throw new RuntimeException(e);
        }
    }

    private static void updateFavorite(BaseMap baseMap, FloorMap floorMap){
        for(Layer layer : floorMap.getLayers()){
            for (PointOfInterest poi : layer.getPoints()){
                for(FavoritePoi favorite : CurrentUser.getUserData().getFavoritePois()){
                    if (favorite.getBaseMapName().toLowerCase().equals(baseMap.getName().toLowerCase())
                            && favorite.getFloorMapName().toLowerCase().equals(floorMap.getName().toLowerCase())
                            && favorite.getLayerName().toLowerCase().equals(layer.getName().toLowerCase())
                            && favorite.getPoiName().toLowerCase().equals(poi.getName().toLowerCase())){
                        poi.setFavorite(true);
                    }
                }
            }
        }

        if(floorMap.getUserLayer() != null && floorMap.getUserLayer().getPoints()!= null){
            for (PointOfInterest poi : floorMap.getUserLayer().getPoints()){
                for(FavoritePoi favorite : CurrentUser.getUserData().getFavoritePois()){
                    if (favorite.getBaseMapName().toLowerCase().equals(baseMap.getName().toLowerCase())
                            && favorite.getFloorMapName().toLowerCase().equals(floorMap.getName().toLowerCase())
                            && favorite.getLayerName().toLowerCase().equals(floorMap.getUserLayer().getName().toLowerCase())
                            && favorite.getPoiName().toLowerCase().equals(poi.getName().toLowerCase())){
                        poi.setFavorite(true);
                    }
                }
            }
        }

    }
}
