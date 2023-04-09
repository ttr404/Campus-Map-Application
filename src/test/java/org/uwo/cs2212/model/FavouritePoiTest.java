package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author Tingrui Zhang
 */
class FavouritePoiTest {

    private FavouritePoi favouritePoi;

    @BeforeEach
    void setUp() {
        favouritePoi = new FavouritePoi();
    }

    @Test
    void testBaseMapName() {
        String baseMapName = "Base Map 1";
        favouritePoi.setBaseMapName(baseMapName);
        assertEquals(baseMapName, favouritePoi.getBaseMapName());

        // Test with empty string
        favouritePoi.setBaseMapName("");
        assertEquals("", favouritePoi.getBaseMapName());

        // Test with null value
        favouritePoi.setBaseMapName(null);
        assertNull(favouritePoi.getBaseMapName());
    }

    @Test
    void testFloorMapName() {
        String floorMapName = "Floor Map 1";
        favouritePoi.setFloorMapName(floorMapName);
        assertEquals(floorMapName, favouritePoi.getFloorMapName());

        // Test with empty string
        favouritePoi.setFloorMapName("");
        assertEquals("", favouritePoi.getFloorMapName());

        // Test with null value
        favouritePoi.setFloorMapName(null);
        assertNull(favouritePoi.getFloorMapName());
    }

    @Test
    void testLayerName() {
        String layerName = "Layer 1";
        favouritePoi.setLayerName(layerName);
        assertEquals(layerName, favouritePoi.getLayerName());

        // Test with empty string
        favouritePoi.setLayerName("");
        assertEquals("", favouritePoi.getLayerName());

        // Test with null value
        favouritePoi.setLayerName(null);
        assertNull(favouritePoi.getLayerName());
    }

    @Test
    void testPoiName() {
        String poiName = "POI 1";
        favouritePoi.setPoiName(poiName);
        assertEquals(poiName, favouritePoi.getPoiName());

        // Test with empty string
        favouritePoi.setPoiName("");
        assertEquals("", favouritePoi.getPoiName());

        // Test with null value
        favouritePoi.setPoiName(null);
        assertNull(favouritePoi.getPoiName());
    }
}
