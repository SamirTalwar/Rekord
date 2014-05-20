package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public final class Property<T, V> {
    private final Key<? super T, V> key;
    private final V value;

    public Property(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        this.key = key;
        this.value = value;
    }

    public Key<? super T, V> key() {
        return key;
    }

    public V value() {
        return value;
    }
}
