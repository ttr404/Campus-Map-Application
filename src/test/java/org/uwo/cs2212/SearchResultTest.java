package org.uwo.cs2212;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.uwo.cs2212.model.FloorMap;
import org.uwo.cs2212.model.PointOfInterest;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Tingrui Zhang
 */
class SearchResultTest {

    private SearchResult searchResult;
    private PointOfInterest poi;
    private FloorMap floorMap;

    @BeforeEach
    void setUp() {
        poi = new PointOfInterest();
        poi.setName("Test POI");
        floorMap = new FloorMap();
        floorMap.setName("Test FloorMap");
        searchResult = new SearchResult(floorMap, poi);
    }

    @AfterEach
    void tearDown() {
        searchResult = null;
    }

    @Test
    void testGetPoi() {
        assertEquals(poi, searchResult.getPoi());
    }

    @Test
    void testSetPoi() {
        PointOfInterest newPoi = new PointOfInterest();
        newPoi.setName("New Test POI");
        searchResult.setPoi(newPoi);
        assertEquals(newPoi, searchResult.getPoi());

        searchResult.setPoi(null);
        assertNull(searchResult.getPoi());
    }

    @Test
    void testGetFloorMap() {
        assertEquals(floorMap, searchResult.getFloorMap());
    }

    @Test
    void testSetFloorMap() {
        FloorMap newFloorMap = new FloorMap();
        newFloorMap.setName("New Test FloorMap");
        searchResult.setFloorMap(newFloorMap);
        assertEquals(newFloorMap, searchResult.getFloorMap());

        searchResult.setFloorMap(null);
        assertNull(searchResult.getFloorMap());
    }

    @Test
    void testToString() {
        String expectedResult = "Floor: Test FloorMap\n" + poi.toString();
        String actualResult = searchResult.toString();
        assertEquals(expectedResult, actualResult);

        //null cases
        searchResult.setFloorMap(null);
        expectedResult = "Floor: null\n" + poi.toString();
        actualResult = searchResult.toString();
        assertEquals(expectedResult, actualResult);

        searchResult.setPoi(null);
        expectedResult = "Floor: null\nnull";
        actualResult = searchResult.toString();
        assertEquals(expectedResult, actualResult);
    }

}
