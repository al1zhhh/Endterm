package com.example.aoi_endka.cache;


import java.time.Duration;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class SimpleCashe {

    private static volatile SimpleCashe instance;

    private static final class CacheEntry {
        private final Object value;
        private final long expiresAtMillis; // 0 = never expires

        private CacheEntry(Object value, long expiresAtMillis) {
            this.value = value;
            this.expiresAtMillis = expiresAtMillis;
        }
    }

    private final Map<String, CacheEntry> store = new ConcurrentHashMap<>();

    private SimpleCashe() {}

    public static SimpleCashe getInstance() {
        if (instance == null) {
            synchronized (SimpleCashe.class) {
                if (instance == null) instance = new SimpleCashe();
            }
        }
        return instance;
    }

    public <T> Optional<T> get(String key, Class<T> clazz) {
        CacheEntry entry = store.get(key);
        if (entry == null) return Optional.empty();

        if (entry.expiresAtMillis > 0 && System.currentTimeMillis() > entry.expiresAtMillis) {
            store.remove(key);
            return Optional.empty();
        }

        return Optional.of(clazz.cast(entry.value));
    }

    public void put(String key, Object value) {
        store.put(key, new CacheEntry(value, 0));
    }

    public void put(String key, Object value, Duration ttl) {
        long expiresAt = System.currentTimeMillis() + ttl.toMillis();
        store.put(key, new CacheEntry(value, expiresAt));
    }

    public void invalidate(String key) {
        store.remove(key);
    }

    public void clear() {
        store.clear();
    }
}
