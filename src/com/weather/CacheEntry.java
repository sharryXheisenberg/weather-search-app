package com.weather;

public class CacheEntry {

    private final String data;
    private final long timestamp;

    public CacheEntry(String data) {
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isExpired(long expiryMillis) {
        return (System.currentTimeMillis() - timestamp) > expiryMillis;
    }
}