package com.noodlesandwich.rekord;

import java.util.HashMap;
import java.util.Map;

public class Rekord<T extends RekordType> {
    private final Map<Key<T, ?>, Object> properties;

    public Rekord(Map<Key<T, ?>, Object> properties) {
        this.properties = properties;
    }

    public static <T extends RekordType> RekordBuilder<T> create() {
        return new RekordBuilder<>();
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<T, V> key) {
        return (V) properties.get(key);
    }

    public static final class RekordBuilder<T extends RekordType> {
        private final Map<Key<T, ?>, Object> properties = new HashMap<>();

        public <V> RekordBuilder<T> with(Key<T, V> key, V value) {
            properties.put(key, value);
            return this;
        }

        public Rekord<T> build() {
            return new Rekord<>(properties);
        }
    }
}
