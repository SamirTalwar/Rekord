package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.Properties;

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
    public boolean test(Properties<T> properties) {
        return underlyingKey.test(properties);
    }

    @Override
    public V get(Properties<T> properties) {
        V value = underlyingKey.get(properties);
        if (value == null) {
            return defaultValue;
        }
        return value;
    }

    @Override
    public Properties<T> set(V value, Properties<T> properties) {
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
