package com.noodlesandwich.rekord.implementation;

import java.util.Iterator;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class PersistentProperties<T> implements Properties<T> {
    private final PMap<Key<T, ?>, Property<T, ?>> properties;

    public PersistentProperties() {
        this(HashTreePMap.<Key<T, ?>, Property<T, ?>>empty());
    }

    private PersistentProperties(PMap<Key<T, ?>, Property<T, ?>> properties) {
        this.properties = properties;
    }

    @Override
    public boolean has(Key<T, ?> key) {
        return properties.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V get(Key<T, V> key) {
        if (!has(key)) {
            return null;
        }

        return (V) properties.get(key).value();
    }

    @Override
    public PersistentProperties<T> set(Property<T, ?> property) {
        Key<T, ?> key = property.key();
        return new PersistentProperties<>(properties.plus(key, property));
    }

    @Override
    public PersistentProperties<T> remove(Key<T, ?> key) {
        return new PersistentProperties<>(
                properties.minus(key)
        );
    }

    @Override
    public Iterator<Property<T, ?>> iterator() {
        return properties.values().iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof PersistentProperties)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        PersistentProperties<T> that = (PersistentProperties<T>) other;
        return properties.equals(that.properties);
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
