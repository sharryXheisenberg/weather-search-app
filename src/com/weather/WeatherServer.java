package com.weather;


import com.sun.net.httpserver.HttpServer;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Properties;

public class WeatherServer {

    public static void main(String[] args) {
        try {
            Properties config = new Properties();
            config.load(new FileInputStream("config.properties"));

            WeatherService weatherService = new WeatherService(config);

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/api/weather", new WeatherHandler(weatherService));

            server.setExecutor(null);
            server.start();

            System.out.println("Weather API Server started on port 8080");
            System.out.println("Example: http://localhost:8080/api/weather?city=London");

        } catch (IOException e) {
            System.err.println("Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
