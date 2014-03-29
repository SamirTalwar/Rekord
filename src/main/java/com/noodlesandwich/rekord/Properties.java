package com.noodlesandwich.rekord;

import java.util.Set;
import org.pcollections.HashTreePMap;
import org.pcollections.HashTreePSet;
import org.pcollections.PMap;

public final class Properties {
    private final PMap<Key<?, ?>, Object> properties;
    private final PMap<Key<?, ?>, Key<?, ?>> assignedKeys;

    public Properties() {
        this(HashTreePMap.<Key<?, ?>, Object>empty(), HashTreePMap.<Key<?, ?>, Key<?, ?>>empty());
    }

    private Properties(PMap<Key<?, ?>, Object> properties, PMap<Key<?, ?>, Key<?, ?>> assignedKeys) {
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

    public <V> Properties with(Property<?, V> property) {
        return new Properties(properties.plus(property.originalKey(), property.value()),
                              assignedKeys.plus(property.originalKey(), property.key()));
    }

    public Properties without(Key<?, ?> key) {
        return new Properties(properties.minus(key),
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
}
