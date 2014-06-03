package com.noodlesandwich.rekord.implementation;

import java.util.Iterator;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class PersistentPropertyMap<T> implements Properties<T>, PropertyMap<T> {
    private final PMap<Key<? super T, ?>, Property<? super T, ?>> properties;

    public PersistentPropertyMap() {
        this(HashTreePMap.<Key<? super T, ?>, Property<? super T, ?>>empty());
    }

    private PersistentPropertyMap(PMap<Key<? super T, ?>, Property<? super T, ?>> properties) {
        this.properties = properties;
    }

    @Override
    public boolean has(Key<? super T, ?> key) {
        return properties.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> V get(Key<? super T, V> key) {
        if (!has(key)) {
            return null;
        }

        return (V) properties.get(key).value();
    }

    @Override
    public PersistentPropertyMap<T> set(Property<? super T, ?> property) {
        Key<? super T, ?> key = property.key();
        return new PersistentPropertyMap<>(properties.plus(key, property));
    }

    @Override
    public PersistentPropertyMap<T> remove(Key<? super T, ?> key) {
        return new PersistentPropertyMap<>(
                properties.minus(key)
        );
    }

    @Override
    public Iterator<Property<? super T, ?>> iterator() {
        return properties.values().iterator();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof PersistentPropertyMap)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        PersistentPropertyMap<T> that = (PersistentPropertyMap<T>) other;
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
