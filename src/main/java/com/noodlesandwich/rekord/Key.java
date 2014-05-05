package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.RekordSerializer;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class Key<T, V> {
    public abstract String name();

    public abstract <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer);

    public <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    public abstract Properties storeTo(Properties properties, V value);

    public abstract V retrieveFrom(Properties properties);

    public abstract <A> void accumulate(V value, RekordSerializer.Serializer<A> serializer);

    public abstract Key<T, ?> original();

    @Override
    public String toString() {
        return name();
    }
}
