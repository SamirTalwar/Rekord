package com.noodlesandwich.rekord;

import java.util.HashMap;
import java.util.Map;

public class Rekord<T extends RekordType> {
    private final String name;
    private final Map<Key<? super T, ?>, Object> properties;

    public Rekord(String name, Map<Key<? super T, ?>, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T extends RekordType> RekordBuilder<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T extends RekordType> RekordBuilder<T> create(String name) {
        return new RekordBuilder<>(name);
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

        //noinspection SimplifiableIfStatement
        if (!(o instanceof Rekord)) {
            return false;
        }

        return properties.equals(((Rekord<T>) o).properties);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        return name + properties.toString();
    }

    public static final class RekordBuilder<T extends RekordType> {
        private final String name;
        private final Map<Key<? super T, ?>, Object> properties = new HashMap<>();

        public RekordBuilder(String name) {
            this.name = name;
        }

        public <V> RekordBuilder<T> with(Key<? super T, V> key, V value) {
            properties.put(key, value);
            return this;
        }

        public Rekord<T> build() {
            return new Rekord<>(name, properties);
        }
    }
}
