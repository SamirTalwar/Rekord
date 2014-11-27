package com.noodlesandwich.rekord.keys;

import java.util.Collections;
import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;

public abstract class OriginalKey<T, V> extends AbstractKey<T, V> {
    public OriginalKey(String name) {
        super(name);
    }

    @Override
    public final boolean test(Properties<T> properties) {
        return properties.has(this);
    }

    @Override
    public final V get(Properties<T> properties) {
        return properties.get(this);
    }

    @Override
    public final Properties<T> set(V value, Properties<T> properties) {
        return properties.set(new Property<>(this, value));
    }

    @Override
    public final Iterator<Key<T, ?>> iterator() {
        return Collections.<Key<T, ?>>singleton(this).iterator();
    }
}
