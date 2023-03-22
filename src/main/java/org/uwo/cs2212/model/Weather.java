package org.uwo.cs2212.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.*;
public class Weather {
    protected double latitude;
    protected double longitude;
    private String apiKey = "90989f1e4b8f4af54e30c4d7ad6a994c";

    public Weather(double latitude, double longitude) throws IOException { // lat = 43.009953, lon = -81.273613
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String toString(){
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(fetchWeather().getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            StringBuilder builder = new StringBuilder();

            String jsonData = response.toString();

            System.out.println(jsonData);

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject weather = jsonObject.getJSONArray("weather").getJSONObject(0);
            String currWeather = weather.getString("main"); // why not description?

            JSONObject temp = (JSONObject) jsonObject.get("main");
            int celsius = (int)(temp.getDouble("temp") - 273.15);
            builder.append("Current weather is ").append(currWeather).append(". Current temperature is ").append(celsius).append(" degrees celsius.");

            return builder.toString();
        } catch (IOException e) {
            System.out.println("HTTP Connection Error!");
            throw new RuntimeException(e);
        }
    };
    public HttpURLConnection fetchWeather() throws IOException {
        String url = "https://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=" + apiKey;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        return con;
    };
}
