package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class DefaultedKey<T, V> extends AbstractKey<T, V> {
    private final Key<T, V> key;
    private final V defaultValue;

    public DefaultedKey(Key<T, V> key, V defaultValue) {
        super(key.name());
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public static <T, V> UnsureDefaultedKey<T, V> wrapping(Key<T, V> key) {
        return new UnsureDefaultedKey<>(key);
    }

    @Override
    public Property<T, V> of(V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V get(PropertyMap<? extends T> properties) {
        if (!properties.has(key)) {
            return defaultValue;
        }
        return properties.get(key);
    }

    @Override
    public boolean test(PropertyMap<? extends T> properties) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E {
        throw new UnsupportedOperationException();
    }

    public static final class UnsureDefaultedKey<T, V> {
        private final Key<T, V> key;

        public UnsureDefaultedKey(Key<T, V> key) {
            this.key = key;
        }

        public DefaultedKey<T, V> defaultingTo(V defaultValue) {
            return new DefaultedKey<>(key, defaultValue);
        }
    }
}
