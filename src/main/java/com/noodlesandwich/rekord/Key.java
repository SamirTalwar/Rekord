package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.NamedKey;

public abstract class Key<T, V> {
    public static <T, V> Key<T, V> named(String name) {
        return new NamedKey<>(name);
    }

    @SuppressWarnings("unchecked")
    public abstract V retrieveFrom(Properties<T> properties);

    public abstract String toString();

    public abstract Property<T, V> of(V value);
}
