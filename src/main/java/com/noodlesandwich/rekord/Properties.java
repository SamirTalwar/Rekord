package com.noodlesandwich.rekord;

import java.util.Set;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public class Properties<T> {
    private final PMap<Key<? super T, ?>, Object> properties;

    public Properties() {
        this(HashTreePMap.<Key<? super T, ?>, Object>empty());
    }

    private Properties(PMap<Key<? super T, ?>, Object> properties) {
        this.properties = properties;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return (V) properties.get(key);
    }

    public boolean contains(Key<T, ?> key) {
        return properties.containsKey(key);
    }

    public Set<Key<? super T, ?>> keys() {
        return properties.keySet();
    }

    public <V> Properties<T> with(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        return new Properties<>(properties.plus(key, value));
    }

    public Properties<T> without(Key<? super T, ?> key) {
        return new Properties<>(properties.minus(key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Properties)) return false;

        Properties other = (Properties) o;
        return properties.equals(other.properties);

    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        return properties.toString();
    }
}
