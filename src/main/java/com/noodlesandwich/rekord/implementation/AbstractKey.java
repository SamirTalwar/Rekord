package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class AbstractKey<T, V> implements Key<T, V> {
    @Override
    public <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    @Override
    public String toString() {
        return name();
    }
}
