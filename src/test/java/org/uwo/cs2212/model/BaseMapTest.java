package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * @author Tingrui Zhang
 */
class BaseMapTest {

    private BaseMap baseMap;

    @BeforeEach
    void setUp() {
        baseMap = new BaseMap();
    }

    @Test
    void testFloorConfigs() {
        FloorMap floorMap1 = new FloorMap();
        FloorMap floorMap2 = new FloorMap();
        List<FloorMap> floorConfigs = Arrays.asList(floorMap1, floorMap2);
        baseMap.setFloorConfigs(floorConfigs);
        assertEquals(floorConfigs, baseMap.getFloorConfigs());

        // Test with empty list
        baseMap.setFloorConfigs(new ArrayList<>());
        assertEquals(new ArrayList<>(), baseMap.getFloorConfigs());

        // Test with null value
        baseMap.setFloorConfigs(null);
        assertNull(baseMap.getFloorConfigs());
    }

    @Test
    void testName() {
        String name = "Base Map 1";
        baseMap.setName(name);
        assertEquals(name, baseMap.getName());

        // Test with null value
        baseMap.setName(null);
        assertNull(baseMap.getName());
    }

    @Test
    void testFloorMaps() {
        FloorMap floorMap1 = new FloorMap();
        FloorMap floorMap2 = new FloorMap();
        List<FloorMap> floorMaps = Arrays.asList(floorMap1, floorMap2);
        baseMap.setFloorMaps(floorMaps);
        assertEquals(floorMaps, baseMap.getFloorMaps());

        // Test with empty list
        baseMap.setFloorMaps(new ArrayList<>());
        assertEquals(new ArrayList<>(), baseMap.getFloorMaps());

        // Test with null value
        baseMap.setFloorMaps(null);
        assertNull(baseMap.getFloorMaps());
    }
}
