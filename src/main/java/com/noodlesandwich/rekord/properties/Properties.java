package com.noodlesandwich.rekord.properties;

import java.util.Set;
import com.noodlesandwich.rekord.implementation.Keys;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeySet;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class Properties<T> {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final KeySet<T> acceptedKeys;
    private final PMap<Key<? super T, ?>, Object> properties;

    public Properties(KeySet<T> acceptedKeys) {
        this(acceptedKeys.originals(), HashTreePMap.<Key<? super T, ?>, Object>empty());
    }

    private Properties(KeySet<T> acceptedKeys, PMap<Key<? super T, ?>, Object> properties) {
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return (V) properties.get(key);
    }

    public boolean has(Key<? super T, ?> key) {
        return properties.containsKey(key.original());
    }

    public KeySet<T> keys() {
        @SuppressWarnings("unchecked")
        Set<KeySet<? super T>> keys = (Set) properties.keySet();
        return Keys.from(keys);
    }

    public KeySet<T> acceptedKeys() {
        return acceptedKeys;
    }

    public Properties<T> with(Property<? super T, ?> property) {
        Key<? super T, ?> key = property.key();
        if (!acceptedKeys.contains(key.original())) {
            throw new IllegalArgumentException(String.format(UnacceptableKeyTemplate, key.name()));
        }

        return new Properties<>(acceptedKeys, properties.plus(key, property.value()));
    }

    public Properties<T> without(Key<? super T, ?> key) {
        return new Properties<>(
                acceptedKeys,
                properties.minus(key)
        );
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
