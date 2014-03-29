package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.Transformers;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class TransformingKey<T, U, V> extends Key<T, V> {
    private final Key<T, U> delegate;
    private final Transformer<U, V> transformer;

    public TransformingKey(Key<T, U> delegate, Transformer<U, V> transformer) {
        this.delegate = delegate;
        this.transformer = transformer;
    }

    @Override
    public <NewV> TransformingKey<T, U, NewV> that(Transformer<V, NewV> transformer) {
        return new TransformingKey<>(delegate, Transformers.compose(transformer, this.transformer));
    }

    @Override
    public Properties storeTo(Properties properties, V value) {
        return properties.with(new Property<>(this, delegate, transformer.transformInput(value)));
    }

    @SuppressWarnings("unchecked")
    @Override
    public V retrieveFrom(Properties properties) {
        return transformer.transformOutput((U) properties.get(delegate));
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
