package com.noodlesandwich.rekord;

import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public final class Key<T, V> {
    private final String name;

    private Key(String name) {
        this.name = name;
    }

    public static <T, V> Key<T, V> named(String name) {
        return new Key<>(name);
    }

    @SuppressWarnings("unchecked")
    public V retrieveFrom(Map<Key<? super T, ?>, Object> properties) {
        return (V) properties.get(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
