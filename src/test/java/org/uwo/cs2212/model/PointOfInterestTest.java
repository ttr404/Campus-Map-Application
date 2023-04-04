package org.uwo.cs2212.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class PointOfInterestTest {

    private PointOfInterest poi;

    @BeforeEach
    void setUp() {
        poi = new PointOfInterest();
    }

    @Test
    void testCoordinates() {
        poi.setX(10.0);
        poi.setY(20.0);
        assertEquals(10.0, poi.getX());
        assertEquals(20.0, poi.getY());
    }

    @Test
    void testName() {

        //normal case
        poi.setName("Test POI");
        assertEquals("Test POI", poi.getName());

        // Test with empty string
        poi.setName("");
        assertEquals("", poi.getName());

        // Test with null value
        poi.setName(null);
        assertNull(poi.getName());
    }

    @Test
    void testRoomNumber() {
        //normal case
        poi.setRoomNumber("R101");
        assertEquals("R101", poi.getRoomNumber());

        // Test with empty string
        poi.setRoomNumber("");
        assertEquals("", poi.getRoomNumber());

        // Test with null value
        poi.setRoomNumber(null);
        assertNull(poi.getRoomNumber());
    }

    @Test
    void testDescription() {
        //normal case
        poi.setDescription("Test description");
        assertEquals("Test description", poi.getDescription());

        // Test with empty string
        poi.setDescription("");
        assertEquals("", poi.getDescription());

        // Test with null value
        poi.setDescription(null);
        assertNull(poi.getDescription());
    }

    @Test
    void testType() {

        //normal case
        poi.setType("Test type");
        assertEquals("Test type", poi.getType());

        // Test with empty string
        poi.setType("");
        assertEquals("", poi.getType());

        // Test with null value
        poi.setType(null);
        assertNull(poi.getType());
    }

    @Test
    void testSelected() {
        poi.setSelected(true);
        assertTrue(poi.isSelected());

        poi.setSelected(false);
        assertFalse(poi.isSelected());
    }

    @Test
    void testFavorite() {
        poi.setFavorite(true);
        assertTrue(poi.isFavorite());

        poi.setFavorite(false);
        assertFalse(poi.isFavorite());
    }

    @Test
    void testToString() {
        // All values set
        poi.setType("Test Type");
        poi.setName("Test Name");
        poi.setDescription("Test Description");
        String expectedResult = "Room Type: Test Type\nRoom Name: Test Name \nTest Description";
        assertEquals(expectedResult, poi.toString());

        // Null type
        poi.setType(null);
        expectedResult = "Room Type: null\nRoom Name: Test Name \nTest Description";
        assertEquals(expectedResult, poi.toString());

        // Null name
        poi.setType("Test Type");
        poi.setName(null);
        expectedResult = "Room Type: Test Type\nRoom Name: null \nTest Description";
        assertEquals(expectedResult, poi.toString());

        // Null description
        poi.setName("Test Name");
        poi.setDescription(null);
        expectedResult = "Room Type: Test Type\nRoom Name: Test Name \nnull";
        assertEquals(expectedResult, poi.toString());

        // All values null
        poi.setType(null);
        poi.setName(null);
        expectedResult = "Room Type: null\nRoom Name: null \nnull";
        assertEquals(expectedResult, poi.toString());
    }

    @Test
    void testEquals() {
        // All values set
        poi.setType("Test Type");
        poi.setName("Test Name");
        poi.setDescription("Test Description");
        String expectedResult = "Room Type: Test Type\nRoom Name: Test Name \nTest Description";
        assertEquals(expectedResult, poi.toString());

        // Null type
        poi.setType(null);
        expectedResult = "Room Type: null\nRoom Name: Test Name \nTest Description";
        assertEquals(expectedResult, poi.toString());

        // Null name
        poi.setType("Test Type");
        poi.setName(null);
        expectedResult = "Room Type: Test Type\nRoom Name: null \nTest Description";
        assertEquals(expectedResult, poi.toString());

        // Null description
        poi.setName("Test Name");
        poi.setDescription(null);
        expectedResult = "Room Type: Test Type\nRoom Name: Test Name \nnull";
        assertEquals(expectedResult, poi.toString());

        // All values null
        poi.setType(null);
        poi.setName(null);
        expectedResult = "Room Type: null\nRoom Name: null \nnull";
        assertEquals(expectedResult, poi.toString());
    }

}
