package com.noodlesandwich.rekord.properties;

import java.util.Objects;
import com.noodlesandwich.rekord.keys.Key;

public final class SimpleProperty<T, V> implements Property<T, V> {
    private static final String FormatString = "(%s: %s)";

    private final Key<T, V> key;
    private final V value;

    public SimpleProperty(Key<T, V> key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Key<T, V> key() {
        return key;
    }

    @Override
    public V value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof SimpleProperty)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        SimpleProperty<T, V> that = (SimpleProperty<T, V>) o;
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
