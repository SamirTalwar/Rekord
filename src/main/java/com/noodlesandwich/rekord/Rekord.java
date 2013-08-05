package com.noodlesandwich.rekord;

import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class Rekord<T extends RekordType> {
    private final String name;
    private final PMap<Key<? super T, ?>, Object> properties;

    public Rekord(String name, PMap<Key<? super T, ?>, Object> properties) {
        this.name = name;
        this.properties = properties;
    }

    public static <T extends RekordType> Rekord.Builder<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T extends RekordType> Rekord.Builder<T> create(String name) {
        return new Rekord.Builder<>(name);
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return (V) properties.get(key);
    }

    public Rekord.Builder<T> but() {
        return new Rekord.Builder<>(name, properties);
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

    public static final class Builder<T extends RekordType> {
        private final String name;
        private PMap<Key<? super T, ?>, Object> properties;

        public Builder(String name) {
            this(name, HashTreePMap.<Key<? super T, ?>, Object>empty());
        }

        public Builder(String name, PMap<Key<? super T, ?>, Object> properties) {
            this.name = name;
            this.properties = properties;
        }

        public <V> Rekord.Builder<T> with(Key<? super T, V> key, V value) {
            if (key == null) {
                throw new NullPointerException("Cannot construct a Rekord property with a null key.");
            }

            if (value == null) {
                throw new NullPointerException("Cannot construct a Rekord property with a null value.");
            }

            properties = properties.plus(key, value);
            return this;
        }

        public Rekord.Builder<T> without(Key<? super T, ?> key) {
            properties = properties.minus(key);
            return this;
        }

        public Rekord<T> build() {
            return new Rekord<>(name, properties);
        }
    }
}
