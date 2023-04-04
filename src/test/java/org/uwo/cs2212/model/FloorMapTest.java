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
class FloorMapTest {

    private FloorMap floorMap;

    @BeforeEach
    void setUp() {
        floorMap = new FloorMap();
    }

    @Test
    void testConfigFileName() {
        String configFileName = "config.json";
        floorMap.setConfigFileName(configFileName);
        assertEquals(configFileName, floorMap.getConfigFileName());

        // Test with empty string
        floorMap.setConfigFileName("");
        assertEquals("", floorMap.getConfigFileName());

        // Test with null value
        floorMap.setConfigFileName(null);
        assertNull(floorMap.getConfigFileName());
    }

    @Test
    void testMapFileName() {
        String mapFileName = "map.png";
        floorMap.setMapFileName(mapFileName);
        assertEquals(mapFileName, floorMap.getMapFileName());

        // Test with empty string
        floorMap.setMapFileName("");
        assertEquals("", floorMap.getMapFileName());

        // Test with null value
        floorMap.setMapFileName(null);
        assertNull(floorMap.getMapFileName());
    }

    @Test
    void testShowLegend() {
        floorMap.setShowLegend(true);
        assertEquals(true, floorMap.isShowLegend());

        floorMap.setShowLegend(false);
        assertEquals(false, floorMap.isShowLegend());
    }

    @Test
    void testLegendPosition() {
        List<Double> legendPosition = Arrays.asList(1.0, 2.0);
        floorMap.setLegendPosition(legendPosition);
        assertEquals(legendPosition, floorMap.getLegendPosition());

        // Test with empty list
        floorMap.setLegendPosition(Collections.emptyList());
        assertEquals(Collections.emptyList(), floorMap.getLegendPosition());

        // Test with null value
        floorMap.setLegendPosition(null);
        assertNull(floorMap.getLegendPosition());
    }

    @Test
    void testLayers() {
        Layer layer1 = new Layer();
        Layer layer2 = new Layer();
        List<Layer> layers = Arrays.asList(layer1, layer2);
        floorMap.setLayers(layers);
        assertEquals(layers, floorMap.getLayers());

        // Test with empty list
        floorMap.setLayers(Collections.emptyList());
        assertEquals(Collections.emptyList(), floorMap.getLayers());

        // Test with null value
        floorMap.setLayers(null);
        assertNull(floorMap.getLayers());
    }

    @Test
    void testName() {
        String name = "Floor 1";
        floorMap.setName(name);
        assertEquals(name, floorMap.getName());

        // Test with empty string
        floorMap.setName("");
        assertEquals("", floorMap.getName());

        // Test with null value
        floorMap.setName(null);
        assertNull(floorMap.getName());
    }

    @Test
    void testUserLayer() {
        UserLayer userLayer = new UserLayer();
        floorMap.setUserLayer(userLayer);
        assertEquals(userLayer, floorMap.getUserLayer());

        // Test with null value
        floorMap.setUserLayer(null);
        assertNull(floorMap.getUserLayer());

    }
}
