package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class OriginalKey<T, V> extends AbstractKey<T, V> {
    private final String name;

    public OriginalKey(String name) {
        this.name = name;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final <P extends T> Properties<P> storeTo(Properties<P> properties, V value) {
        return properties.with(new Property<P, V>(this, value));
    }

    @Override
    public final <P extends T> V retrieveFrom(Properties<P> properties) {
        return properties.get(this);
    }

    @Override
    public final <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(this, transformer);
    }

    @Override
    public final Key<T, V> original() {
        return this;
    }
}
