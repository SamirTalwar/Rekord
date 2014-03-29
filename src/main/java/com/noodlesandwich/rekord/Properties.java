package com.noodlesandwich.rekord;

import java.util.Set;
import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.OrderedPSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

public final class Properties {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final PSet<Key<?, ?>> allowedKeys;
    private final PMap<Key<?, ?>, Object> properties;
    private final PMap<Key<?, ?>, Key<?, ?>> assignedKeys;

    public Properties(PSet<Key<?, ?>> allowedKeys) {
        this(allowedKeys,
             HashTreePMap.<Key<?, ?>, Object>empty(),
             HashTreePMap.<Key<?, ?>, Key<?, ?>>empty());
    }

    private Properties(PSet<Key<?, ?>> allowedKeys,
                       PMap<Key<?, ?>, Object> properties,
                       PMap<Key<?, ?>, Key<?, ?>> assignedKeys)
    {
        this.allowedKeys = allowedKeys;
        this.properties = properties;
        this.assignedKeys = assignedKeys;
    }

    public Object get(Key<?, ?> key) {
        return properties.get(key);
    }

    public boolean contains(Key<?, ?> key) {
        return properties.containsKey(key);
    }

    @SuppressWarnings("unchecked")
    public <T> Set<Key<? super T, ?>> keys() {
        return (Set) HashTreePSet.from(assignedKeys.values());
    }

    public Properties with(Property property) {
        Key<?, ?> originalKey = property.originalKey();
        if (!allowedKeys.contains(originalKey)) {
            throw new IllegalArgumentException(String.format(UnacceptableKeyTemplate, originalKey));
        }

        return new Properties(
                allowedKeys,
                properties.plus(originalKey, property.value()),
                assignedKeys.plus(originalKey, property.key()));
    }

    public Properties without(Key<?, ?> key) {
        return new Properties(
                allowedKeys,
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

    public static PSet<Key<?, ?>> originalKeys(Key<?, ?>... keys) {
        PSet<Key<?, ?>> keyCollection = OrderedPSet.empty();
        for (Key<?, ?> key : keys) {
            keyCollection = keyCollection.plus(key.original());
        }
        return keyCollection;
    }
}
