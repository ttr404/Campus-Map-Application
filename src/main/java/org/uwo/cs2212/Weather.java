package org.uwo.cs2212;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.*;

/**
 * A class that retrieves weather information from the OpenWeatherMap API.
 *
 * @author Tingrui Zhang
 * @author Binchi Zhang
 */
public class Weather {
    private final JSONObject cachedData;
    protected double latitude;
    protected double longitude;
    private final String apiKey = "90989f1e4b8f4af54e30c4d7ad6a994c";
    private String icon;

    /**
     * Constructs a new Weather object with the specified latitude and longitude.
     *
     * @param latitude  the latitude of the location for which to retrieve weather information
     * @param longitude the longitude of the location for which to retrieve weather information
     * @throws IOException if there was an error retrieving the weather data
     */
    public Weather(double latitude, double longitude) throws IOException {
        this.latitude = latitude;
        this.longitude = longitude;
        this.cachedData = retrieveData();
    }


    /**
     * Retrieves the current weather description for the location specified in the constructor.
     *
     * @return a String representing the current weather description, or "---" if there was an error retrieving the data
     */
    public String getWeather() {
        JSONObject data = retrieveData();

        if (data.has("error")) {
            return "---";
        }

        JSONObject weather = retrieveData().getJSONArray("weather").getJSONObject(0);
        String currWeather = weather.getString("description");
        icon = weather.getString("icon");

        // Split the string apart in order to capitalize the first letter of the weather
        String firstCharCurrWeather = currWeather.substring(0, 1);
        String remainingCurrWeather = currWeather.substring(1);

        // Return the current weather with the first letter capitalized
        return firstCharCurrWeather.toUpperCase() + remainingCurrWeather;
    }

    /**
     * Retrieves the current temperature for the location specified in the constructor.
     *
     * @return a Double representing the current temperature in Celsius, or Double.NaN if there was an error retrieving the data
     */
    public Double getTemp() {
        JSONObject data = retrieveData();

        // Check if there's an error (no internet connection)
        if (data.has("error")) {
            return Double.NaN;
        }
        JSONObject temp = (JSONObject) retrieveData().get("main");
        double celsius = temp.getDouble("temp");
        return celsius;
    }

    /**
     * Retrieves the JSON data for the weather information from the OpenWeatherMap API.
     *
     * @return a JSONObject representing the weather data
     */
    public JSONObject retrieveData() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(fetchWeather().getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            String jsonData = response.toString();

            JSONObject jsonObject = new JSONObject(jsonData);
            return jsonObject;
        } catch (IOException e) {
            System.out.println("HTTP Connection Error: " + e.getMessage());
            JSONObject errorObject = new JSONObject();
            errorObject.put("error", "No internet connection");
            return errorObject;
        }
    }

    /**
     * Retrieves an HttpURLConnection for the weather information from the OpenWeatherMap API.
     *
     * @return an HttpURLConnection object that can be used to retrieve the weather data
     * @throws IOException if there was an error connecting to the API
     */
    public HttpURLConnection fetchWeather() throws IOException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    /**
     * Retrieves an ImageView for the specified weather icon.
     *
     * @param icon a String representing the weather icon code
     * @return an ImageView object for the specified icon
     * @throws IOException if there was an error retrieving the icon image
     */
    public ImageView grabImage(String icon) throws IOException {
        String imageUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
        URL url = new URL(imageUrl);
        Image image = new Image(url.toString());
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    /**
     * Retrieves the current weather icon code for the location specified in the constructor.
     *
     * @return a String representing the current weather icon code
     */
    public String getIcon() {
        return icon;
    }
}
