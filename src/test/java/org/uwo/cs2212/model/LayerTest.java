package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class LayerTest {

    private Layer layer;

    @BeforeEach
    void setUp() {
        layer = new Layer();
    }

    @Test
    void testName() {
        String name = "Layer 1";
        layer.setName(name);
        assertEquals(name, layer.getName());

        // Test with empty string
        layer.setName("");
        assertEquals("", layer.getName());

        // Test with null value
        layer.setName(null);
        assertNull(layer.getName());
    }

    @Test
    void testColor() {
        String color = "red";
        layer.setColor(color);
        assertEquals(color, layer.getColor());

        // Test with empty string
        layer.setColor("");
        assertEquals("", layer.getColor());

        // Test with null value
        layer.setColor(null);
        assertNull(layer.getColor());
    }

    @Test
    void testFont() {
        String font = "Arial";
        layer.setFont(font);
        assertEquals(font, layer.getFont());

        // Test with empty string
        layer.setFont("");
        assertEquals("", layer.getFont());

        // Test with null value
        layer.setFont(null);
        assertNull(layer.getFont());
    }

    @Test
    void testSize() {
        double size = 12.0;
        layer.setSize(size);
        assertEquals(size, layer.getSize());

        // Test with negative size
        double negativeSize = -5.0;
        layer.setSize(negativeSize);
        assertEquals(negativeSize, layer.getSize());
    }

    @Test
    void testPoints() {
        PointOfInterest poi1 = new PointOfInterest();
        PointOfInterest poi2 = new PointOfInterest();
        List<PointOfInterest> points = Arrays.asList(poi1, poi2);
        layer.setPoints(points);
        assertEquals(points, layer.getPoints());

        // Test with empty list
        layer.setPoints(Collections.emptyList());
        assertEquals(Collections.emptyList(), layer.getPoints());

        // Test with null value
        layer.setPoints(null);
        assertNull(layer.getPoints());
    }

    @Test
    void testLayerType() {
        String layerType = "POI";
        layer.setLayerType(layerType);
        assertEquals(layerType, layer.getLayerType());

        // Test with empty string
        layer.setLayerType("");
        assertEquals("", layer.getLayerType());

        // Test with null value
        layer.setLayerType(null);
        assertNull(layer.getLayerType());
    }

    @Test
    void testHideLayer() {
        layer.setHideLayer(true);
        assertTrue(layer.isHideLayer());

        layer.setHideLayer(false);
        assertFalse(layer.isHideLayer());
    }
}
