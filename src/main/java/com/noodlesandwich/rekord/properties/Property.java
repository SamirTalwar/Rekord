package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.Key;

public final class Property {
    private final Key<?, ?> key;
    private final Object value;

    public Property(Key<?, ?> key, Object value) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        this.key = key;
        this.value = value;
    }

    public Key<?, ?> key() {
        return key;
    }

    public Object value() {
        return value;
    }
}
