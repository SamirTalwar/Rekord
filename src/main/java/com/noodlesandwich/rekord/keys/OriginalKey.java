package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.SimpleProperty;
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
    public final Property<T, V> of(V value) {
        return new SimpleProperty<>(this, value);
    }

    @Override
    public final <P extends T> V get(Properties<P> properties) {
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
