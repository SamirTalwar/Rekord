package com.noodlesandwich.rekord;

public final class Property<T, V> {
    private final Key<T, V> key;
    private final V value;

    public Property(Key<T, V> key, V value) {
        this.key = key;
        this.value = value;
    }

    public Key<T, V> key() {
        return key;
    }

    public V value() {
        return value;
    }
}
