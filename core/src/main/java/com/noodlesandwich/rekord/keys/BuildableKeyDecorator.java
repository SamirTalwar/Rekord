package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.Properties;

public final class BuildableKeyDecorator<T, V> extends DelegatingKey<T, V> implements BuildableKey<T, V> {
    private final Key<T, V> key;
    private final V builder;

    public BuildableKeyDecorator(Key<T, V> key, V builder) {
        super(key.name(), key);
        this.key = key;
        this.builder = builder;
    }

    @Override
    public boolean test(Properties<T> properties) {
        return key.test(properties);
    }

    @Override
    public V get(Properties<T> properties) {
        return key.get(properties);
    }

    @Override
    public Properties<T> set(V value, Properties<T> properties) {
        return key.set(value, properties);
    }

    @Override
    public V builder() {
        return builder;
    }
}
