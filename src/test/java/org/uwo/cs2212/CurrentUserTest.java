package org.uwo.cs2212;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uwo.cs2212.model.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class CurrentUserTest {

    private BaseMap baseMap;
    private FloorMap floorMap;
    private PointOfInterest poi;

    @BeforeEach
    void setUp() {
        baseMap = new BaseMap();
        baseMap.setName("Test BaseMap");
        floorMap = new FloorMap();
        floorMap.setName("Test FloorMap");
        poi = new PointOfInterest();
        poi.setName("Test POI");
        CurrentUser.setUsername("testUser");
        CurrentUser.setMapConfig(ConfigUtil.loadMapConfig(CampusMapApplication.class.getResource("map-config.json")));
    }

    @Test
    void testIsAdmin() {
        CurrentUser.setUsername("admin");
        assertTrue(CurrentUser.isAdmin());
        CurrentUser.setUsername("Admin");
        assertTrue(CurrentUser.isAdmin());
        CurrentUser.setUsername("user");
        assertFalse(CurrentUser.isAdmin());
        CurrentUser.setUsername(null);
        assertFalse(CurrentUser.isAdmin());
    }

    @Test
    void testUsernameGetterAndSetter() {
        CurrentUser.setUsername("testUser");
        assertEquals("testUser", CurrentUser.getUsername());
        CurrentUser.setUsername(null);
        assertNull(CurrentUser.getUsername());
    }

    @Test
    void testCurrentSelectedPoiGetterAndSetter() {
        CurrentUser.setCurrentSelectedPoi(poi);
        assertEquals(poi, CurrentUser.getCurrentSelectedPoi());
        CurrentUser.setCurrentSelectedPoi(null);
        assertNull(CurrentUser.getCurrentSelectedPoi());
    }

    @Test
    void testUserDataGetterAndSetter() {
        UserData userData = new UserData();
        CurrentUser.setUserData(userData);
        assertEquals(userData, CurrentUser.getUserData());
        CurrentUser.setUserData(null);
        assertNull(CurrentUser.getUserData());
    }

    @Test
    void testAddPoi() {
        CurrentUser.setUsername("testUser");
        CurrentUser.setUserData(null);
        CurrentUser.addPoi(baseMap, floorMap, poi);
        UserData userData = CurrentUser.getUserData();
        assertNotNull(userData);
        assertEquals(1, userData.getUserLayers().size());
    }

    @Test
    void testAddPoiToExistingUserLayer() {
        CurrentUser.setUsername("testUser");
        CurrentUser.setUserData(null);
        CurrentUser.addPoi(baseMap, floorMap, poi);
        PointOfInterest anotherPoi = new PointOfInterest();
        anotherPoi.setName("Another Test POI");
        CurrentUser.addPoi(baseMap, floorMap, anotherPoi);
        UserData userData = CurrentUser.getUserData();
        assertNotNull(userData);
        assertEquals(1, userData.getUserLayers().size());
        assertEquals(2, userData.getUserLayers().get(0).getPoints().size());
    }

    @Test
    void testAddPoiWithNullBaseMap() {
        CurrentUser.setUsername("testUser");
        CurrentUser.setMapConfig(null);
        CurrentUser.setUserData(null);
        CurrentUser.addPoi(null, floorMap, poi);

    }

    @Test
    void testAddPoiWithNullFloorMap() {
        CurrentUser.setUsername("testUser");
        CurrentUser.setUserData(null);
        CurrentUser.addPoi(baseMap, null, poi);
        UserData userData = CurrentUser.getUserData();
        assertEquals(0, userData.getUserLayers().size());
        assertEquals(0, userData.getFavoritePois().size());
    }

    @Test
    void testAddPoiWithNullPoi() {
        CurrentUser.setUsername("testUser");
        CurrentUser.setUserData(null);
        CurrentUser.addPoi(baseMap, floorMap, null);
        UserData userData = CurrentUser.getUserData();
        assertEquals(0, userData.getUserLayers().size());
        assertEquals(0, userData.getFavoritePois().size());
    }
    @Test
    void testSaveUserData() {
        CurrentUser.setUsername("testUser");
        UserData userData = new UserData();
        CurrentUser.setUserData(userData);
        CurrentUser.addPoi(baseMap, floorMap, poi);
        assertTrue(CurrentUser.saveUserData());
    }

    @Test
    void testSaveUserDataWithNoUsername() {
        CurrentUser.setUsername(null);
        UserData userData = new UserData();
        CurrentUser.setUserData(userData);
        assertThrows(NullPointerException.class, () -> {
            CurrentUser.saveUserData();
        });
    }

    @Test
    void testSaveUserDataWithNoUserData() {
        CurrentUser.setUsername("testUser");
        CurrentUser.setUserData(null);
        assertThrows(NullPointerException.class, () -> {
            CurrentUser.saveUserData();
        });
    }

    @Test
    void testSaveUserDataWithAdminUser() {
        CurrentUser.setUsername("admin");
        UserData userData = new UserData();
        CurrentUser.setUserData(userData);
        assertTrue(CurrentUser.saveUserData());
    }

    @Test
    void testRemoveSelectedPoiNormalCase() {
        CurrentUser.addPoi(baseMap, floorMap, poi);
        CurrentUser.setCurrentSelectedPoi(poi);
        CurrentUser.removeSelectedPOI();
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, CurrentUser.getUserData());
        assertNotNull(userLayer);
        assertFalse(userLayer.getPoints().contains(poi));
    }

    @Test
    void testRemoveSelectedPoiWithNullBaseMap() {
        CurrentUser.addPoi(baseMap, floorMap, poi);
        CurrentUser.setCurrentSelectedPoi(poi);
        CurrentUser.setCurrentBaseMap(null);
        CurrentUser.removeSelectedPOI();
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, CurrentUser.getUserData());
        assertNotNull(userLayer);
        assertTrue(userLayer.getPoints().contains(poi));
    }

    @Test
    void testRemoveSelectedPoiWithNullFloorMap() {
        CurrentUser.addPoi(baseMap, floorMap, poi);
        CurrentUser.setCurrentSelectedPoi(poi);
        CurrentUser.setCurrentFloorMap(null);
        CurrentUser.removeSelectedPOI();
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, CurrentUser.getUserData());
        assertNotNull(userLayer);
        assertTrue(userLayer.getPoints().contains(poi));
    }

    @Test
    void testRemoveSelectedPoiWithNullPoi() {
        CurrentUser.addPoi(baseMap, floorMap, poi);
        CurrentUser.setCurrentSelectedPoi(null);
        CurrentUser.removeSelectedPOI();
        UserLayer userLayer = UserData.findUserLayer(baseMap, floorMap, CurrentUser.getUserData());
        assertNotNull(userLayer);
        assertTrue(userLayer.getPoints().contains(poi));
    }

    @Test
    void testRemoveSelectedPoiWithEmptyUserData() {
        CurrentUser.setUserData(null);
        CurrentUser.setCurrentSelectedPoi(poi);
        CurrentUser.removeSelectedPOI();
        assertNull(CurrentUser.getUserData());
    }
}

