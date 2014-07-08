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
    public <R extends T> boolean test(Properties<R> properties) {
        return key.test(properties);
    }

    @Override
    public <R extends T> V get(Properties<R> properties) {
        return key.get(properties);
    }

    @Override
    public <R extends T> Properties<R> set(V value, Properties<R> properties) {
        return key.set(value, properties);
    }

    @Override
    public V builder() {
        return builder;
    }
}
