package com.noodlesandwich.rekord.implementation;

import java.util.Collections;
import java.util.Set;
import com.noodlesandwich.rekord.keys.Key;

public abstract class AbstractKey<T, V> implements Key<T, V> {
    private final String name;

    public AbstractKey(String name) {
        if (name == null) {
            throw new NullPointerException("The name of a key must not be null.");
        }
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final boolean contains(Key<T, ?> key) {
        return equals(key);
    }

    @Override
    public final Set<Key<T, ?>> toSet() {
        return Collections.<Key<T, ?>>singleton(this);
    }

    @Override
    public final String toString() {
        return name();
    }
}
