package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uwo.cs2212.CurrentUser;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class UserDataTest {

    private UserData userData;
    private BaseMap baseMap;
    private FloorMap floorMap;
    private PointOfInterest poi;

    @BeforeEach
    void setUp() {
        userData = new UserData();
        baseMap = new BaseMap();
        baseMap.setName("Test BaseMap");
        floorMap = new FloorMap();
        floorMap.setName("Test FloorMap");
        poi = new PointOfInterest();
        poi.setName("Test POI");
    }

    @Test
    void testFindUserLayerNotFound() {
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, userData);
        assertNull(userLayer);
    }

    @Test
    void testFindUserLayerWithNullUserData() {
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, null);
        assertNull(null);
    }

    @Test
    void testFindUserLayerWithNullUserLayers() {
        userData.setUserLayers(null);
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, userData);
        assertNull(userLayer);
    }

    @Test
    void testEmptyUserLayers() {
        userData.setUserLayers(new ArrayList<>());
        assertEquals(0, userData.getUserLayers().size());
    }

    @Test
    void testEmptyFavoritePois() {
        userData.setFavoritePois(new ArrayList<>());
        assertEquals(0, userData.getFavoritePois().size());
    }

    @Test
    void testUserLayersGetterAndSetter() {
        List<UserLayer> userLayers = new ArrayList<>();
        UserLayer userLayer = new UserLayer();
        userLayer.setName("Test UserLayer");
        userLayers.add(userLayer);
        userData.setUserLayers(userLayers);
        assertEquals(userLayers, userData.getUserLayers());
    }

    @Test
    void testAddPoi() {
        userData.addPoi(baseMap, floorMap, poi);
        UserLayer userLayer = userData.getUserLayers().get(0);
        PointOfInterest addedPoi = userLayer.getPoints().get(0);
        assertEquals(poi.getName(), addedPoi.getName());
    }

    @Test
    void testUpdateUserLayers() {
        userData.addPoi(baseMap, floorMap, poi);
        UserLayer userLayer = userData.getUserLayers().get(0);
        userLayer.setName("Updated Test UserLayer");
        userData.updateUserLayers(userLayer);
        UserLayer updatedUserLayer = userData.getUserLayers().get(0);
        assertEquals("Updated Test UserLayer", updatedUserLayer.getName());
    }

    @Test
    void testRemoveFavourite() {
        // Set up the current user configuration
        CurrentUser.setUsername("testUser");
        UserData userData = new UserData();
        CurrentUser.setUserData(userData);

        // Set up base map, floor map, and point of interest
        BaseMap baseMap = new BaseMap();
        baseMap.setName("TestBaseMap");
        FloorMap floorMap = new FloorMap();
        floorMap.setName("TestFloorMap");
        PointOfInterest poi = new PointOfInterest();
        poi.setName("TestFavoritePoi");
        poi.setFavorite(true);

        // Add a favorite POI
        userData.addPoi(baseMap, floorMap, poi);
        FavoritePoi favoritepoi = new FavoritePoi(baseMap.getName(), floorMap.getName(), null, poi.getName());
        userData.getFavoritePois().add(favoritepoi);
        // Remove the favorite POI
        userData.removeFavourite(poi, poi.getName(), baseMap.getName(), floorMap.getName());

        // Check if the favorite POI was removed
        assertFalse(userData.getFavoritePois().contains(favoritepoi));

        // Check if the favorite flag was changed
        assertFalse(poi.isFavorite());
    }

    @Test
    void testFindUserLayer() {
        userData.addPoi(baseMap, floorMap, poi);
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, userData);
        assertNotNull(userLayer);
        assertEquals("User layer", userLayer.getName());
    }

    @Test
    void testFavoritePoisGetterAndSetter() {
        List<FavoritePoi> favoritePois = new ArrayList<>();
        FavoritePoi favoritePoi = new FavoritePoi();
        favoritePoi.setPoiName("Test FavoritePoi");
    }
    // Additional test cases

    @Test
    void testAddPoiToExistingUserLayer() {
        // Add a POI to create a new UserLayer
        userData.addPoi(baseMap, floorMap, poi);

        // Add a new POI to the existing UserLayer
        PointOfInterest poi2 = new PointOfInterest();
        poi2.setName("Test POI 2");
        userData.addPoi(baseMap, floorMap, poi2);

        UserLayer userLayer = userData.getUserLayers().get(0);
        assertEquals(2, userLayer.getPoints().size());
        assertEquals(poi2.getName(), userLayer.getPoints().get(1).getName());
    }

    @Test
    void testUpdateNonExistingUserLayers() {
        UserLayer nonExistingUserLayer = new UserLayer();
        nonExistingUserLayer.setName("Non-existing UserLayer");
        userData.updateUserLayers(nonExistingUserLayer);
        assertFalse(userData.getUserLayers().contains(nonExistingUserLayer));
    }

    @Test
    void testRemoveNonExistingFavourite() {
        // Attempt to remove a non-existing favorite POI
        userData.removeFavourite(poi, poi.getName(), baseMap.getName(), floorMap.getName());
        assertTrue(userData.getFavoritePois().isEmpty());
    }

    @Test
    void testAddPoiNullInput() {
        assertThrows(NullPointerException.class, () -> userData.addPoi(null, null, null));
    }

    @Test
    void testRemoveFavouriteWithIncorrectBaseAndFloorMapName() {
        // Set up the current user configuration
        CurrentUser.setUsername("testUser");
        UserData userData = new UserData();
        CurrentUser.setUserData(userData);

        // Set up base map, floor map, and point of interest
        BaseMap baseMap = new BaseMap();
        baseMap.setName("TestBaseMap");
        FloorMap floorMap = new FloorMap();
        floorMap.setName("TestFloorMap");
        PointOfInterest poi = new PointOfInterest();
        poi.setName("TestFavoritePoi");
        poi.setFavorite(true);

        // Add a favorite POI
        userData.addPoi(baseMap, floorMap, poi);
        FavoritePoi favoritePoi = new FavoritePoi(baseMap.getName(), floorMap.getName(), null, poi.getName());
        userData.getFavoritePois().add(favoritePoi);

        // Attempt to remove the favorite POI with incorrect base and floor map names
        userData.removeFavourite(poi, poi.getName(), "IncorrectBaseMap", "IncorrectFloorMap");

        // Check if the favorite POI still exists
        assertTrue(userData.getFavoritePois().contains(favoritePoi));
        assertTrue(poi.isFavorite());
    }

}
