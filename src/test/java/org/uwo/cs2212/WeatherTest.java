package org.uwo.cs2212;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * JUnit5 Testing class for Weather API
 *
 * @author Binchi Zhang
 */

class WeatherTest {

    private Weather weather;

    private final double testLatitude = 45.0;
    private final double testLongitude = -75.0;
    private HttpURLConnection connection;
    private final String apiKey = "90989f1e4b8f4af54e30c4d7ad6a994c";


    @BeforeEach
    void setUp() throws IOException {
        URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + testLatitude + "&lon=" + testLongitude + "&appid=" + apiKey);
        connection = (HttpURLConnection) url.openConnection();
    }

    @Test
    public void testStatusCode() throws IOException {
        int expectedStatusCode = 200;
        int actualStatusCode = connection.getResponseCode();
        assertEquals(expectedStatusCode, actualStatusCode);
    }

    @Test
    public void testJsonResponse() throws IOException{
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/json");

        assertEquals(HttpURLConnection.HTTP_OK, connection.getResponseCode());
        assertTrue(connection.getContentType().contains("json"));

        ObjectMapper mapper = new ObjectMapper();
        Object responseObject = mapper.readValue(connection.getInputStream(), Object.class);
        // perform additional assertions on the response object, if needed
    }

}
