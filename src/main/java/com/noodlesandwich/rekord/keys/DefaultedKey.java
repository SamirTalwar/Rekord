package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.PropertyMap;

public final class DefaultedKey<T, V> extends DelegatingKey<T, V> {
    private final Key<T, V> underlyingKey;
    private final V defaultValue;

    private DefaultedKey(Key<T, V> underlyingKey, V defaultValue) {
        super(underlyingKey.name(), underlyingKey);
        this.underlyingKey = underlyingKey;
        this.defaultValue = defaultValue;
    }

    public static <T, V> UnsureDefaultedKey<T, V> wrapping(Key<T, V> key) {
        if (key == null) {
            throw new NullPointerException("The underlying key of a DefaultedKey must not be null.");
        }
        return new UnsureDefaultedKey<>(key);
    }

    @Override
    public <R extends T> boolean test(PropertyMap<R> properties) {
        return underlyingKey.test(properties);
    }

    @Override
    public <R extends T> V get(PropertyMap<R> properties) {
        V value = underlyingKey.get(properties);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public <R extends T> PropertyMap<R> set(V value, PropertyMap<R> properties) {
        return underlyingKey.set(value, properties);
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
