package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.TransformingKey;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class TransformingProperty<T, U, V> implements Property<T, U> {
    private final TransformingKey<T, U, V> key;
    private final V value;
    private final Transformer<U, V> transformer;

    public TransformingProperty(TransformingKey<T, U, V> key, V value, Transformer<U, V> transformer) {
        if (key == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null key.");
        }

        if (value == null) {
            throw new NullPointerException("Cannot construct a Rekord property with a null value.");
        }

        this.key = key;
        this.value = value;
        this.transformer = transformer;
    }

    @Override
    public Key<T, U> key() {
        return key.original();
    }

    @Override
    public U value() {
        return transformer.transformInput(value);
    }
}
