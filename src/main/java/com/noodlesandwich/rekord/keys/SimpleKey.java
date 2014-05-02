package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class SimpleKey<T, V> extends Key<T, V> {
    private final String name;

    private SimpleKey(String name) {
        this.name = name;
    }

    public static <T, V> Key<T, V> named(String name) {
        return new SimpleKey<>(name);
    }

    @Override
    public <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(this, transformer);
    }

    @Override
    public Properties storeTo(Properties properties, V value) {
        return properties.with(new Property(this, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public V retrieveFrom(Properties properties) {
        return (V) properties.get(this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Key<T, ?> original() {
        return this;
    }

    @Override
    public String toString() {
        return name;
    }
}
