package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.Properties;

public abstract class ForwardingKey<T, V> extends DelegatingKey<T, V> {
    private final Key<T, V> key;

    public ForwardingKey(String name, Key<T, V> key) {
        super(name, key);
        this.key = key;
    }

    @Override
    public final <R extends T> boolean test(Properties<R> properties) {
        return key.test(properties);
    }

    @Override
    public final <R extends T> V get(Properties<R> properties) {
        return key.get(properties);
    }

    @Override
    public final <R extends T> Properties<R> set(V value, Properties<R> properties) {
        return key.set(value, properties);
    }
}
