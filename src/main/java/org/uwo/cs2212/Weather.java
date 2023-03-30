package org.uwo.cs2212;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.json.*;

public class Weather {
    private final JSONObject cachedData;
    protected double latitude;
    protected double longitude;
    private String apiKey = "90989f1e4b8f4af54e30c4d7ad6a994c";
    private String icon;

    public Weather(double latitude, double longitude) throws IOException {
        this.latitude = latitude;
        this.longitude = longitude;
        this.cachedData = retrieveData();
    }

    public String getWeather(){
        JSONObject data = retrieveData();

        if (data.has("error")) {
            return "---";
        }

        JSONObject weather = retrieveData().getJSONArray("weather").getJSONObject(0);
        String currWeather = weather.getString("description");
        icon = weather.getString("icon");
        return currWeather;
    }

    public Double getTemp(){
        JSONObject data = retrieveData();

        // Check if there's an error (no internet connection)
        if (data.has("error")) {
            return Double.NaN;
        }
        JSONObject temp = (JSONObject) retrieveData().get("main");
        double celsius = temp.getDouble("temp");
        return celsius;
    }

    public JSONObject retrieveData(){
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
        } catch (IOException e){
            System.out.println("HTTP Connection Error: " + e.getMessage());
            JSONObject errorObject = new JSONObject();
            errorObject.put("error", "No internet connection");
            return errorObject;
        }
    }

    public HttpURLConnection fetchWeather() throws IOException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey + "&units=metric";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        return con;
    }

    public ImageView grabImage(String icon) throws IOException {
        String imageUrl = "https://openweathermap.org/img/wn/" + icon + "@2x.png";
        URL url = new URL(imageUrl);
        Image image = new Image(url.toString());
        ImageView imageView = new ImageView(image);
        return imageView;
    }

    public String getIcon(){
        return icon;
    }
}
