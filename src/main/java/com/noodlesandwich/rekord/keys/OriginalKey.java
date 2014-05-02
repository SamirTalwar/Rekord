package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Property;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class OriginalKey<T, V> extends Key<T, V> {
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
}
