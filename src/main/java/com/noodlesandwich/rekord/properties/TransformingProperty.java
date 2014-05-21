package com.noodlesandwich.rekord.properties;

import java.util.Objects;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.TransformingKey;
import com.noodlesandwich.rekord.transformers.Transformer;

public final class TransformingProperty<T, U, V> implements Property<T, U> {
    private static final String FormatString = "transformation over (%s: %s)";

    private final TransformingKey<T, U, V> key;
    private final V value;
    private final Transformer<U, V> transformer;

    public TransformingProperty(TransformingKey<T, U, V> key, V value, Transformer<U, V> transformer) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TransformingProperty)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        TransformingProperty<T, U, V> that = (TransformingProperty<T, U, V>) o;
        return key.equals(that.key) && value.equals(that.value) && transformer.equals(that.transformer);

    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value, transformer);
    }

    @Override
    public String toString() {
        return String.format(FormatString, key, value);
    }
}
