package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.Properties;

public final class BuildableKeyDecorator<T, V, B> extends DelegatingKey<T, V> implements BuildableKey<T, V, B> {
    private final Key<T, V> key;
    private final B builder;

    public BuildableKeyDecorator(Key<T, V> key, B builder) {
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
    public B builder() {
        return builder;
    }
}
