package com.noodlesandwich.rekord.properties;

import java.util.Objects;
import com.noodlesandwich.rekord.keys.Key;

public final class Property<T, V> {
    private static final String FormatString = "(%s: %s)";

    private final Key<T, V> key;
    private final V value;

    public Property(Key<T, V> key, V value) {
        if (key == null) {
            throw new NullPointerException("A property cannot have a null key.");
        }

        if (value == null) {
            throw new NullPointerException("A property cannot have a null value.");
        }

        this.key = key;
        this.value = value;
    }

    public Key<T, V> key() {
        return key;
    }

    public V value() {
        return value;
    }

    @SuppressWarnings("unchecked")
    public <U> Property<U, V> forAnotherRekord() {
        return (Property<U, V>) this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Property)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Property<T, V> that = (Property<T, V>) o;
        return key.equals(that.key) && value.equals(that.value);

    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return String.format(FormatString, key, value);
    }
}
