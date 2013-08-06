package com.noodlesandwich.rekord;

import java.util.Set;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class Properties {
    private final PMap<Key<?, ?>, Object> properties;

    public Properties() {
        this(HashTreePMap.<Key<?, ?>, Object>empty());
    }

    private Properties(PMap<Key<?, ?>, Object> properties) {
        this.properties = properties;
    }

    public Object get(Key<?, ?> key) {
        return properties.get(key);
    }

    public boolean contains(Key<?, ?> key) {
        return properties.containsKey(key);
    }

    public Set<Key<?, ?>> keys() {
        return properties.keySet();
    }

    public <V> Properties with(Property<?, V> property) {
        return new Properties(properties.plus(property.key(), property.value()));
    }

    public Properties without(Key<?, ?> key) {
        return new Properties(properties.minus(key));
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
