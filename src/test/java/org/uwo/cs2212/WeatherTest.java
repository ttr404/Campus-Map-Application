package org.uwo.cs2212;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * JUnit5 Testing class for Weather API
 *
 * @author Tingrui Zhang
 */
@ExtendWith(MockitoExtension.class)
class WeatherTest {

    @Mock
    private HttpURLConnection mockHttpURLConnection;

    private Weather weather;
    private double testLatitude = 45.0;
    private double testLongitude = -75.0;

    @BeforeEach
    void setUp() throws IOException {
        weather = new Weather(0, 0);

    };

    @Test
    void getWeather() {
        String expected = "Clear sky";
        String actual = weather.getWeather();
        assertEquals(expected, actual);
    }
    @Test
    void getTemp() {
        Double expected = 25.0;
        Double actual = weather.getTemp();
        assertEquals(expected, actual);
    }

    @Test
    void getIcon() {
        String expected = "01d";
        String actual = weather.getIcon();
        assertEquals(expected, actual);
    }

    @Test
    void testGetWeather_WithValidData() throws IOException {
        String validWeatherData = "{\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"main\":{\"temp\":22.51}}";
        InputStream inputStream = new ByteArrayInputStream(validWeatherData.getBytes());
        when(mockHttpURLConnection.getInputStream()).thenReturn(inputStream);

        String result = weather.getWeather();
        assertEquals("Clear sky", result);
    }

    @Test
    void testGetWeather_WithError() throws IOException {
        when(mockHttpURLConnection.getInputStream()).thenThrow(IOException.class);

        String result = weather.getWeather();
        assertEquals("---", result);
    }

    @Test
    void testGetTemp_WithValidData() throws IOException {
        String validWeatherData = "{\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"main\":{\"temp\":22.51}}";
        InputStream inputStream = new ByteArrayInputStream(validWeatherData.getBytes());
        when(mockHttpURLConnection.getInputStream()).thenReturn(inputStream);

        Double result = weather.getTemp();
        assertEquals(22.51, result);
    }

    @Test
    void testGetTemp_WithError() throws IOException {
        when(mockHttpURLConnection.getInputStream()).thenThrow(IOException.class);

        Double result = weather.getTemp();
        assertEquals(Double.NaN, result);
    }

    @Test
    void testRetrieveData_WithValidData() throws IOException {
        String validWeatherData = "{\"weather\":[{\"id\":800,\"main\":\"Clear\",\"description\":\"clear sky\",\"icon\":\"01d\"}],\"main\":{\"temp\":22.51}}";
        InputStream inputStream = new ByteArrayInputStream(validWeatherData.getBytes());
        when(mockHttpURLConnection.getInputStream()).thenReturn(inputStream);

        JSONObject result = weather.retrieveData();
        assertEquals("clear sky", result.getJSONArray("weather").getJSONObject(0).getString("description"));
        assertEquals(22.51, result.getJSONObject("main").getDouble("temp"));
    }

    @Test
    void testRetrieveData_WithError() throws IOException {
        when(mockHttpURLConnection.getInputStream()).thenThrow(IOException.class);

        JSONObject result = weather.retrieveData();
        assertEquals("No internet connection", result.getString("error"));
    }
}
