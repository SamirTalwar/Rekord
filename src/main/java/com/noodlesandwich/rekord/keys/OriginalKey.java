package com.noodlesandwich.rekord.keys;

import java.util.Collections;
import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;

public abstract class OriginalKey<T, V> extends AbstractKey<T, V> {
    public OriginalKey(String name) {
        super(name);
    }

    @Override
    public final <R extends T> boolean test(PropertyMap<R> properties) {
        return properties.has(this);
    }

    @Override
    public final <R extends T> V get(PropertyMap<R> properties) {
        return properties.get(this);
    }

    @Override
    public final <R extends T> PropertyMap<R> set(V value, PropertyMap<R> properties) {
        return properties.set(new Property<>(this, value));
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return Collections.<Key<? super T, ?>>singleton(this).iterator();
    }
}
