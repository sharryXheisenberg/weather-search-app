package com.weather;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Properties;

public class OpenWeatherClient {

    private final String apiKey;
    private final String apiUrl;

    public OpenWeatherClient(Properties config) {
        this.apiKey = config.getProperty("api.key");
        this.apiUrl = config.getProperty("api.url");
    }

    public String fetchWeather(String city) {
        HttpURLConnection connection = null;

        try {
            // Build API URL with parameters
            String encodedCity = URLEncoder.encode(city, "UTF-8");
            String urlString = apiUrl + "?q=" + encodedCity + "&appid=" + apiKey + "&units=metric";

            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            int responseCode = connection.getResponseCode();

            if (responseCode == 200) {
                // Read response
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(connection.getInputStream())
                );
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                return response.toString();

            } else if (responseCode == 404) {
                System.err.println("City not found: " + city);
                return null;
            } else {
                System.err.println("API error: " + responseCode);
                return null;
            }

        } catch (Exception e) {
            System.err.println("Error fetching weather: " + e.getMessage());
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}