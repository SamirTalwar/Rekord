package com.noodlesandwich.rekord.implementation;

import java.util.Iterator;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import org.pcollections.HashTreePMap;
import org.pcollections.PMap;

public final class LimitedPropertyMap<T> implements Properties<T>, PropertyMap<T> {
    private static final String UnacceptableKeyTemplate = "The key \"%s\" is not a valid key for this Rekord.";

    private final Keys<T> acceptedKeys;
    private final PMap<Key<? super T, ?>, Property<? super T, ?>> properties;

    public LimitedPropertyMap(Keys<T> acceptedKeys) {
        this(acceptedKeys, HashTreePMap.<Key<? super T, ?>, Property<? super T, ?>>empty());
    }

    private LimitedPropertyMap(Keys<T> acceptedKeys, PMap<Key<? super T, ?>, Property<? super T, ?>> properties) {
        this.acceptedKeys = acceptedKeys;
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
    public LimitedPropertyMap<T> set(Property<? super T, ?> property) {
        Key<? super T, ?> key = property.key();
        if (!acceptedKeys.contains(key)) {
            throw new IllegalArgumentException(String.format(UnacceptableKeyTemplate, key.name()));
        }
        return new LimitedPropertyMap<>(acceptedKeys, properties.plus(key, property));
    }

    @Override
    public LimitedPropertyMap<T> remove(Key<? super T, ?> key) {
        return new LimitedPropertyMap<>(
                acceptedKeys,
                properties.minus(key)
        );
    }

    public Keys<T> acceptedKeys() {
        return acceptedKeys;
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

        if (!(other instanceof LimitedPropertyMap)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        LimitedPropertyMap<T> that = (LimitedPropertyMap<T>) other;
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
