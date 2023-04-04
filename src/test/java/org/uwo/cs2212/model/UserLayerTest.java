package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class UserLayerTest {

    private UserLayer userLayer;

    @BeforeEach
    void setUp() {
        userLayer = new UserLayer();
    }

    @Test
    void testEquals() {
        // Normal case
        UserLayer otherNormal = new UserLayer();
        otherNormal.setFloorName("Test Floor");
        otherNormal.setBaseName("Test Base");

        userLayer.setFloorName("Test Floor");
        userLayer.setBaseName("Test Base");

        assertTrue(userLayer.equals(otherNormal));

        // Null case
        UserLayer otherNull = null;
        assertFalse(userLayer.equals(otherNull));

        // Empty case
        UserLayer otherEmpty = new UserLayer();
        otherEmpty.setFloorName("");
        otherEmpty.setBaseName("");

        userLayer.setFloorName("");
        userLayer.setBaseName("");

        assertTrue(userLayer.equals(otherEmpty));

        // Mismatched cases
        UserLayer otherMismatch1 = new UserLayer();
        otherMismatch1.setFloorName("Different Floor");
        otherMismatch1.setBaseName("Different Base");

        assertFalse(userLayer.equals(otherMismatch1));

        UserLayer otherMismatch2 = new UserLayer();
        otherMismatch2.setFloorName("Test Floor");
        otherMismatch2.setBaseName(null);

        assertFalse(userLayer.equals(otherMismatch2));

        UserLayer otherMismatch3 = new UserLayer();
        otherMismatch3.setFloorName(null);
        otherMismatch3.setBaseName("Test Base");

        assertFalse(userLayer.equals(otherMismatch3));
    }

    @Test
    void testGetAndSetFloorName() {
        // Normal case
        userLayer.setFloorName("Test Floor");
        assertEquals("Test Floor", userLayer.getFloorName());

        // Null case
        userLayer.setFloorName(null);
        assertNull(userLayer.getFloorName());

        // Empty case
        userLayer.setFloorName("");
        assertEquals("", userLayer.getFloorName());
    }

    @Test
    void testGetAndSetBaseName() {
        // Normal case
        userLayer.setBaseName("Test Base");
        assertEquals("Test Base", userLayer.getBaseName());

        // Null case
        userLayer.setBaseName(null);
        assertNull(userLayer.getBaseName());

        // Empty case
        userLayer.setBaseName("");
        assertEquals("", userLayer.getBaseName());
    }
}
