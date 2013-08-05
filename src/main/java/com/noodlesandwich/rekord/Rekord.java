package com.noodlesandwich.rekord;

import java.util.HashMap;
import java.util.Map;

public class Rekord<T extends RekordType> {
    private final Map<Key<? super T, ?>, Object> properties;

    public Rekord(Map<Key<? super T, ?>, Object> properties) {
        this.properties = properties;
    }

    public static <T extends RekordType> RekordBuilder<T> create() {
        return new RekordBuilder<>();
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return (V) properties.get(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Rekord)) {
            return false;
        }

        return properties.equals(((Rekord<T>) o).properties);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public static final class RekordBuilder<T extends RekordType> {
        private final Map<Key<? super T, ?>, Object> properties = new HashMap<>();

        public <V> RekordBuilder<T> with(Key<? super T, V> key, V value) {
            properties.put(key, value);
            return this;
        }

        public Rekord<T> build() {
            return new Rekord<>(properties);
        }
    }
}
