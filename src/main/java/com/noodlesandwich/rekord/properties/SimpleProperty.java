package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public final class SimpleProperty<T, V> implements Property<T, V> {
    private final Key<? super T, V> key;
    private final V value;

    public SimpleProperty(Key<? super T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        this.key = key;
        this.value = value;
    }

    @Override
    public Key<? super T, V> key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }
}
