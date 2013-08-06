package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.NamedKey;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class Key<T, V> {
    public static <T, V> Key<T, V> named(String name) {
        return new NamedKey<>(name);
    }

    public abstract <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer);

    public <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    public abstract Properties storeTo(Properties properties, V value);

    public abstract V retrieveFrom(Properties properties);

    public abstract String toString();
}
