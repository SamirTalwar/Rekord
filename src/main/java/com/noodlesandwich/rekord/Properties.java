package com.noodlesandwich.rekord;

import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.OrderedPSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

public final class Properties<T> {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final PSet<Key<? super T, ?>> acceptedKeys;
    private final PMap<Key<? super T, ?>, Object> properties;
    private final PMap<Key<? super T, ?>, Key<? super T, ?>> assignedKeys;

    public Properties(PSet<Key<? super T, ?>> acceptedKeys) {
        this(originalKeys(acceptedKeys),
             HashTreePMap.<Key<? super T, ?>, Object>empty(),
             HashTreePMap.<Key<? super T, ?>, Key<? super T, ?>>empty());
    }

    private Properties(PSet<Key<? super T, ?>> acceptedKeys,
                       PMap<Key<? super T, ?>, Object> properties,
                       PMap<Key<? super T, ?>, Key<? super T, ?>> assignedKeys)
    {
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
        this.assignedKeys = assignedKeys;
    }

    @SuppressWarnings("unchecked")
    public <V> V get(Key<? super T, V> key) {
        return (V) properties.get(key);
    }

    public boolean contains(Key<? super T, ?> key) {
        return properties.containsKey(key);
    }

    public PSet<Key<? super T, ?>> keys() {
        return HashTreePSet.from(assignedKeys.values());
    }

    public PSet<Key<? super T, ?>> acceptedKeys() {
        return acceptedKeys;
    }

    public Properties<T> with(Property property) {
        @SuppressWarnings("unchecked")
        Key<? super T, ?> key = (Key<? super T, ?>) property.key();
        @SuppressWarnings("unchecked")
        Key<? super T, ?> originalKey = (Key<? super T, ?>) property.originalKey();
        if (!acceptedKeys.contains(originalKey)) {
            throw new IllegalArgumentException(String.format(UnacceptableKeyTemplate, originalKey.name()));
        }

        return new Properties<>(
                acceptedKeys,
                properties.plus(originalKey, property.value()),
                assignedKeys.plus(originalKey, key));
    }

    public Properties<T> without(Key<? super T, ?> key) {
        return new Properties<>(
                acceptedKeys,
                properties.minus(key),
                assignedKeys.minus(key));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Properties)) return false;

        @SuppressWarnings("unchecked")
        Properties<T> other = (Properties<T>) o;
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

    private static <T> PSet<Key<? super T, ?>> originalKeys(Iterable<Key<? super T, ?>> keys) {
        PSet<Key<? super T, ?>> keyCollection = OrderedPSet.empty();
        for (Key<? super T, ?> key : keys) {
            keyCollection = keyCollection.plus(key.original());
        }
        return keyCollection;
    }
}
