package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;
import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.OrderedPSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

public final class Properties<T> {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final PSet<Key<? super T, ?>> acceptedKeys;
    private final PMap<Key<? super T, ?>, Object> properties;

    public Properties(PSet<Key<? super T, ?>> acceptedKeys) {
        this(originalKeys(acceptedKeys), HashTreePMap.<Key<? super T, ?>, Object>empty());
    }

    private Properties(PSet<Key<? super T, ?>> acceptedKeys, PMap<Key<? super T, ?>, Object> properties) {
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

    public PSet<Key<? super T, ?>> keys() {
        return HashTreePSet.from(properties.keySet());
    }

    public PSet<Key<? super T, ?>> acceptedKeys() {
        return acceptedKeys;
    }

    public Properties<T> with(Property<T, ?> property) {
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

    private static <T> PSet<Key<? super T, ?>> originalKeys(Iterable<Key<? super T, ?>> keys) {
        PSet<Key<? super T, ?>> originalKeys = OrderedPSet.empty();
        for (Key<? super T, ?> key : keys) {
            originalKeys = originalKeys.plus(key.original());
        }
        return originalKeys;
    }
}
