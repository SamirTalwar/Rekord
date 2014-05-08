package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class OriginalKey<T, V> extends Key<T, V> {
    private final String name;

    public OriginalKey(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(this, transformer);
    }

    @Override
    public <P extends T> Properties<P> storeTo(Properties<P> properties, V value) {
        return properties.with(new Property(this, value));
    }

    @Override
    public <P extends T> V retrieveFrom(Properties<P> properties) {
        return properties.get(this);
    }

    @Override
    public Key<T, ?> original() {
        return this;
    }
}
