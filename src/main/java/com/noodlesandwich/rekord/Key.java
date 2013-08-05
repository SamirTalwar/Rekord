package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.DefaultedKey;
import com.noodlesandwich.rekord.keys.NamedKey;
import com.noodlesandwich.rekord.keys.PropertyKey;

public abstract class Key<T, V> {
    public static <T, V> Key<T, V> named(String name) {
        return new NamedKey<>(new PropertyKey<T, V>(), name);
    }

    public Key<T, V> defaultingTo(V defaultValue) {
        return new DefaultedKey<>(this, defaultValue);
    }

    public Property<T, V> of(V value) {
        return new Property<>(this, value);
    }

    public V retrieveFrom(Properties<T> properties) {
        return retrieveFrom(properties, this);
    }

    public abstract V retrieveFrom(Properties<T> properties, Key<T, V> surrogateKey);

    public abstract String toString();
}
