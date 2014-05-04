package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.transformers.Transformer;
import com.noodlesandwich.rekord.transformers.Transformers;

public final class TransformingKey<T, U, V> extends Key<T, V> {
    private final Key<T, U> original;
    private final Transformer<U, V> transformer;

    public TransformingKey(Key<T, U> original, Transformer<U, V> transformer) {
        this.original = original;
        this.transformer = transformer;
    }

    @Override
    public String name() {
        return original.name();
    }

    @Override
    public <NewV> TransformingKey<T, U, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(original, Transformers.compose(transformer, this.transformer));
    }

    @Override
    public Properties storeTo(Properties properties, V value) {
        return properties.with(new Property(this, transformer.transformInput(value)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public V retrieveFrom(Properties properties) {
        return transformer.transformOutput((U) properties.get(original));
    }

    @SuppressWarnings("unchecked")
    @Override
    public Key<T, ?> original() {
        return original;
    }
}
