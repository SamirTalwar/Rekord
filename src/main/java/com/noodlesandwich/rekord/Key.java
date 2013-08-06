package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.transformers.Transformer;

public final class Key<T, V> {
    private final String name;
    private final Transformer<Object, V> transformer;

    public Key(String name, Transformer<Object, V> transformer) {
        this.name = name;
        this.transformer = transformer;
    }

    public static <T, V> Key<T, V> named(String name) {
        return new Key<>(name, new Transformer<Object, V>() {
            @Override public Object transformInput(V value) {
                return value;
            }

            @SuppressWarnings("unchecked")
            @Override public V transformOutput(Object value) {
                return (V) value;
            }
        });
    }

    public <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer) {
        return new Key<>(name, Transformers.compose(transformer, this.transformer));
    }

    public <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    public Property<T, V> of(V value) {
        return new Property<>(this, value);
    }

    public V transform(Object value) {
        return transformer.transformOutput(value);
    }

    public String toString() {
        return name;
    }
}
