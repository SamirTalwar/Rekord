package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;

public final class SimpleKey<T, V> extends OriginalKey<T,V> {
    private final String name;

    private SimpleKey(String name) {
        this.name = name;
    }

    public static <T, V> Key<T, V> named(String name) {
        return new SimpleKey<>(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
