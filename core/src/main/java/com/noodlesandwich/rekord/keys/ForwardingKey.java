package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.properties.Properties;

public abstract class ForwardingKey<T, V> extends DelegatingKey<T, V> {
    private final Key<T, V> key;

    public ForwardingKey(String name, Key<T, V> key) {
        super(name, key);
        this.key = key;
    }

    @Override
    public final boolean test(Properties<T> properties) {
        return key.test(properties);
    }

    @Override
    public final V get(Properties<T> properties) {
        return key.get(properties);
    }

    @Override
    public final Properties<T> set(V value, Properties<T> properties) {
        return key.set(value, properties);
    }
}
