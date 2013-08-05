package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.transformers.Transformer;

public final class Key<T, V> {
    private final String name;
    private final Transformer<V, V> transformer;

    public Key(String name, Transformer<V, V> transformer) {
        this.name = name;
        this.transformer = transformer;
    }

    public static <T, V> Key<T, V> named(String name) {
        return new Key<>(name, Transformers.<V>identity());
    }

    public Key<T, V> that(Transformer<V, V> transformer) {
        return new Key<>(name, Transformers.compose(transformer, this.transformer));
    }

    public Key<T, V> then(Transformer<V, V> transformer) {
        return that(transformer);
    }

    public Property<T, V> of(V value) {
        return new Property<>(this, value);
    }

    public V retrieveFrom(Properties<T> properties) {
        return transformer.transform(properties.get(this));
    }

    public String toString() {
        return name;
    }
}
