package com.weather;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class WeatherHandler implements HttpHandler {

    private final WeatherService weatherService;

    public WeatherHandler(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type");

        if (exchange.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
            exchange.sendResponseHeaders(204, -1);
            return;
        }

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {
            sendResponse(exchange, 405, "{\"error\": \"Method not allowed\"}");
            return;
        }

        try {
            String query = exchange.getRequestURI().getQuery();
            String city = extractCityFromQuery(query);

            if (city == null || city.trim().isEmpty()) {
                sendResponse(exchange, 400, "{\"error\": \"City parameter is required\"}");
                return;
            }

            String weatherJson = weatherService.getWeather(city);

            if (weatherJson == null) {
                sendResponse(exchange, 404, "{\"error\": \"City not found\"}");
                return;
            }

            sendResponse(exchange, 200, weatherJson);

        } catch (Exception e) {
            System.err.println("Error handling request: " + e.getMessage());
            sendResponse(exchange, 500, "{\"error\": \"Internal server error\"}");
        }
    }

    private String extractCityFromQuery(String query) {
        if (query == null) return null;

        Map<String, String> params = new HashMap<>();
        String[] pairs = query.split("&");

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }

        return params.get("city");
    }

    private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}