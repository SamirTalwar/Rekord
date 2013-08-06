package com.noodlesandwich.rekord;

public final class Property<T, V> {
    private final Key<T, V> key;
    private final Key<T, ?> actualKey;
    private final V value;

    public Property(Key<T, V> key, V value) {
        this(key, key, value);
    }

    public Property(Key<T, V> key, Key<T, ?> actualKey, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        this.key = key;
        this.actualKey = actualKey;
        this.value = value;
    }

    public Key<T, V> key() {
        return key;
    }

    public Key<T, ?> actualKey() {
        return actualKey;
    }

    public V value() {
        return value;
    }
}
