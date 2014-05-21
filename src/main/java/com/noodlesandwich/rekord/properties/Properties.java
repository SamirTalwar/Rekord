package com.noodlesandwich.rekord.properties;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import com.noodlesandwich.rekord.implementation.Keys;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeySet;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class Properties<T> implements PropertySet<T> {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final KeySet<T> acceptedKeys;
    private final PMap<Key<? super T, ?>, Property<? super T, ?>> properties;

    public Properties(KeySet<T> acceptedKeys) {
        this(acceptedKeys.originals(), HashTreePMap.<Key<? super T, ?>, Property<? super T, ?>>empty());
    }

    private Properties(KeySet<T> acceptedKeys, PMap<Key<? super T, ?>, Property<? super T, ?>> properties) {
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        if (!has(key)) {
            return null;
        }

        return (V) properties.get(key.original()).value();
    }

    public boolean has(Key<? super T, ?> key) {
        return properties.containsKey(key.original());
    }

    public KeySet<T> keys() {
        Set<KeySet<? super T>> keys = new HashSet<>();
        for (Property<? super T, ?> property : properties.values()) {
            keys.add(property.key());
        }
        return Keys.from(keys);
    }

    public KeySet<T> acceptedKeys() {
        return acceptedKeys;
    }

    public Properties<T> with(Property<? super T, ?> property) {
        Key<? super T, ?> key = property.key();
        Key<? super T, ?> originalKey = key.original();
        Object value = property.value();

        if (value == null) {
            throw new NullPointerException("A property cannot have a null value.");
        }

        if (!acceptedKeys.contains(originalKey)) {
            throw new IllegalArgumentException(String.format(UnacceptableKeyTemplate, key.name()));
        }

        return new Properties<>(acceptedKeys, properties.plus(originalKey, property));
    }

    public Properties<T> without(Key<? super T, ?> key) {
        return new Properties<>(
                acceptedKeys,
                properties.minus(key.original())
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

        if (!(other instanceof Properties)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Properties<T> that = (Properties<T>) other;
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
