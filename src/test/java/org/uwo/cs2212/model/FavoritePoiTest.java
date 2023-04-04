package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author Tingrui Zhang
 */
class FavoritePoiTest {

    private FavoritePoi favoritePoi;

    @BeforeEach
    void setUp() {
        favoritePoi = new FavoritePoi();
    }

    @Test
    void testBaseMapName() {
        String baseMapName = "Base Map 1";
        favoritePoi.setBaseMapName(baseMapName);
        assertEquals(baseMapName, favoritePoi.getBaseMapName());

        // Test with empty string
        favoritePoi.setBaseMapName("");
        assertEquals("", favoritePoi.getBaseMapName());

        // Test with null value
        favoritePoi.setBaseMapName(null);
        assertNull(favoritePoi.getBaseMapName());
    }

    @Test
    void testFloorMapName() {
        String floorMapName = "Floor Map 1";
        favoritePoi.setFloorMapName(floorMapName);
        assertEquals(floorMapName, favoritePoi.getFloorMapName());

        // Test with empty string
        favoritePoi.setFloorMapName("");
        assertEquals("", favoritePoi.getFloorMapName());

        // Test with null value
        favoritePoi.setFloorMapName(null);
        assertNull(favoritePoi.getFloorMapName());
    }

    @Test
    void testLayerName() {
        String layerName = "Layer 1";
        favoritePoi.setLayerName(layerName);
        assertEquals(layerName, favoritePoi.getLayerName());

        // Test with empty string
        favoritePoi.setLayerName("");
        assertEquals("", favoritePoi.getLayerName());

        // Test with null value
        favoritePoi.setLayerName(null);
        assertNull(favoritePoi.getLayerName());
    }

    @Test
    void testPoiName() {
        String poiName = "POI 1";
        favoritePoi.setPoiName(poiName);
        assertEquals(poiName, favoritePoi.getPoiName());

        // Test with empty string
        favoritePoi.setPoiName("");
        assertEquals("", favoritePoi.getPoiName());

        // Test with null value
        favoritePoi.setPoiName(null);
        assertNull(favoritePoi.getPoiName());
    }
}
