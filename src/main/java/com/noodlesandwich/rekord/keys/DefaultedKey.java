package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class DefaultedKey<T, V> extends AbstractKey<T, V> {
    private final Key<T, V> key;
    private final V defaultValue;

    private DefaultedKey(Key<T, V> key, V defaultValue) {
        super(key.name());
        this.key = key;
        this.defaultValue = defaultValue;
    }

    public static <T, V> UnsureDefaultedKey<T, V> wrapping(Key<T, V> key) {
        if (key == null) {
            throw new NullPointerException("The underlying key of a DefaultedKey must not be null.");
        }
        return new UnsureDefaultedKey<>(key);
    }

    @Override
    public Property<T, ?> of(V value) {
        return key.of(value);
    }

    @Override
    public V get(PropertyMap<? extends T> properties) {
        V value = key.get(properties);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public boolean test(PropertyMap<? extends T> properties) {
        return key.test(properties);
    }

    @Override
    public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    public static final class UnsureDefaultedKey<T, V> {
        private final Key<T, V> key;

        private UnsureDefaultedKey(Key<T, V> key) {
            this.key = key;
        }

        public DefaultedKey<T, V> defaultingTo(V defaultValue) {
            if (defaultValue == null) {
                throw new NullPointerException("The default value of a DefaultedKey must not be null.");
            }
            return new DefaultedKey<>(key, defaultValue);
        }
    }
}
