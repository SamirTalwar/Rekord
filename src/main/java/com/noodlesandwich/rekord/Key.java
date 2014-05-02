package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class Key<T, V> {
    public abstract <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer);

    public <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    public abstract Properties storeTo(Properties properties, V value);

    public abstract V retrieveFrom(Properties properties);

    public abstract Key<T, ?> original();

    public abstract String toString();
}
