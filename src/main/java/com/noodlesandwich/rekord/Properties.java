package com.noodlesandwich.rekord;

import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.OrderedPSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

public final class Properties {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final PSet<Key<?, ?>> acceptedKeys;
    private final PMap<Key<?, ?>, Object> properties;
    private final PMap<Key<?, ?>, Key<?, ?>> assignedKeys;

    public Properties(PSet<Key<?, ?>> acceptedKeys) {
        this(originalKeys(acceptedKeys),
             HashTreePMap.<Key<?, ?>, Object>empty(),
             HashTreePMap.<Key<?, ?>, Key<?, ?>>empty());
    }

    private Properties(PSet<Key<?, ?>> acceptedKeys,
                       PMap<Key<?, ?>, Object> properties,
                       PMap<Key<?, ?>, Key<?, ?>> assignedKeys)
    {
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
        this.assignedKeys = assignedKeys;
    }

    public Object get(Key<?, ?> key) {
        return properties.get(key);
    }

    public boolean contains(Key<?, ?> key) {
        return properties.containsKey(key);
    }

    public PSet<Key<?, ?>> keys() {
        return HashTreePSet.from(assignedKeys.values());
    }

    public PSet<Key<?, ?>> acceptedKeys() {
        return acceptedKeys;
    }

    public Properties with(Property property) {
        Key<?, ?> originalKey = property.originalKey();
        if (!acceptedKeys.contains(originalKey)) {
            throw new IllegalArgumentException(String.format(UnacceptableKeyTemplate, originalKey.name()));
        }

        return new Properties(
                acceptedKeys,
                properties.plus(originalKey, property.value()),
                assignedKeys.plus(originalKey, property.key()));
    }

    public Properties without(Key<?, ?> key) {
        return new Properties(
                acceptedKeys,
                properties.minus(key),
                assignedKeys.minus(key));
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

    private static PSet<Key<?, ?>> originalKeys(Iterable<Key<?, ?>> keys) {
        PSet<Key<?, ?>> keyCollection = OrderedPSet.empty();
        for (Key<?, ?> key : keys) {
            keyCollection = keyCollection.plus(key.original());
        }
        return keyCollection;
    }
}
