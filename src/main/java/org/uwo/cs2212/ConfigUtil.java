package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.uwo.cs2212.model.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigUtil {
    public static MapConfig loadMapConfig(URL url){
        try {
            //read json file data to String
            byte[] mapJsonData = Files.readAllBytes(Path.of(url.toURI()));
            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            MapConfig mapConfig = objectMapper.readValue(mapJsonData, MapConfig.class);
            for (BaseMap baseMap: mapConfig.getBaseMaps()) {
                for (FloorMap floorConfig: baseMap.getFloorConfigs()) {
                    url = ConfigUtil.class.getResource(floorConfig.getConfigFileName());
                    FloorMap floorMap = loadFloorMap(url);
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
    public static FloorMap loadFloorMap(URL url){
        try {
            //read json file data to String
            byte[] mapJsonData = Files.readAllBytes(Path.of(url.toURI()));
            //create ObjectMapper instance
            ObjectMapper objectMapper = new ObjectMapper();
            FloorMap floorMap = objectMapper.readValue(mapJsonData, FloorMap.class);

            return floorMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserList loadUserList(URL url){
        try {
            byte[] userJsonData = Files.readAllBytes(Path.of(url.toURI()));
            ObjectMapper objectMapper = new ObjectMapper();
            UserList userList = objectMapper.readValue(userJsonData, UserList.class);

            return userList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
