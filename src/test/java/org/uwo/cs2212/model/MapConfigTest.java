package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author Tingrui Zhang
 */
class MapConfigTest {

    private MapConfig mapConfig;

    @BeforeEach
    void setUp() {
        mapConfig = new MapConfig();
    }

    @Test
    void testBaseMaps() {
        BaseMap baseMap1 = new BaseMap();
        BaseMap baseMap2 = new BaseMap();
        List<BaseMap> baseMaps = Arrays.asList(baseMap1, baseMap2);
        mapConfig.setBaseMaps(baseMaps);
        assertEquals(baseMaps, mapConfig.getBaseMaps());

        // Test with empty list
        mapConfig.setBaseMaps(Collections.emptyList());
        assertEquals(Collections.emptyList(), mapConfig.getBaseMaps());

        // Test with null value
        mapConfig.setBaseMaps(null);
        assertNull(mapConfig.getBaseMaps());
    }
}
