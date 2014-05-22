package com.noodlesandwich.rekord.implementation;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.properties.Property;

public abstract class AbstractKey<T, V> implements Key<T, V> {
    private final String name;

    public AbstractKey(String name) {
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final Property<T, V> of(V value) {
        return new Property<>(this, value);
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return Collections.<Key<? super T, ?>>singleton(this).iterator();
    }

    @Override
    public final boolean contains(Key<? super T, ?> key) {
        return equals(key);
    }

    @Override
    public final Set<Key<? super T, ?>> toSet() {
        return Collections.<Key<? super T, ?>>singleton(this);
    }

    @Override
    public final String toString() {
        return name();
    }
}
