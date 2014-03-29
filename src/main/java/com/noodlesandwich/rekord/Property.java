package com.noodlesandwich.rekord;

public final class Property<T, V> {
    private final Key<T, ?> key;
    private final Key<T, V> originalKey;
    private final V value;

    public Property(Key<T, V> key, V value) {
        this(key, key, value);
    }

    public Property(Key<T, ?> key, Key<T, V> originalKey, V value) {
        if (originalKey == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        this.key = key;
        this.originalKey = originalKey;
        this.value = value;
    }

    public Key<T, ?> key() {
        return key;
    }

    public Key<T, V> originalKey() {
        return originalKey;
    }

    public V value() {
        return value;
    }
}
