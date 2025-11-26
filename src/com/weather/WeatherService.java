package com.weather;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class WeatherService {

    private final OpenWeatherClient weatherClient;
    private final Map<String, CacheEntry> cache;
    private final long cacheExpiryMillis;
    private final int maxCacheEntries;

    public WeatherService(Properties config) {
        this.weatherClient = new OpenWeatherClient(config);
        this.cache = new LinkedHashMap<>(16, 0.75f, true);

        int expiryMinutes = Integer.parseInt(config.getProperty("cache.expiry.minutes", "10"));
        this.cacheExpiryMillis = expiryMinutes * 60 * 1000L;
        this.maxCacheEntries = Integer.parseInt(config.getProperty("cache.max.entries", "100"));
    }

    public String getWeather(String city) {
        String cityKey = city.toLowerCase().trim();

        CacheEntry cachedEntry = cache.get(cityKey);

        if (cachedEntry != null && !cachedEntry.isExpired(cacheExpiryMillis)) {
            System.out.println("Cache HIT for city: " + city);
            return cachedEntry.getData();
        }

        System.out.println("Cache MISS for city: " + city);
        String weatherData = weatherClient.fetchWeather(city);

        if (weatherData != null) {
            addToCache(cityKey, weatherData);
        }

        return weatherData;
    }

    private void addToCache(String city, String data) {
        if (cache.size() >= maxCacheEntries) {
            String oldestKey = cache.keySet().iterator().next();
            cache.remove(oldestKey);
            System.out.println("Cache full - removed oldest entry: " + oldestKey);
        }

        cache.put(city, new CacheEntry(data));
        System.out.println("Added to cache: " + city);
    }
}