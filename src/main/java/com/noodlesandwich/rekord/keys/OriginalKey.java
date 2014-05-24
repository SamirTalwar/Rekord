package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;

public abstract class OriginalKey<T, V> extends AbstractKey<T, V> {
    public OriginalKey(String name) {
        super(name);
    }

    @Override
    public final Property<T, V> of(V value) {
        return new Property<>(this, value);
    }

    @Override
    public final V get(PropertyMap<? extends T> properties) {
        return properties.get(this);
    }

    @Override
    public final boolean test(PropertyMap<? extends T> properties) {
        return properties.has(this);
    }
}
