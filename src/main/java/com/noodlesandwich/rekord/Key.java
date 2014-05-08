package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.RekordSerializer;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class Key<T, V> implements Named {
    public abstract <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer);

    public <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    public abstract <P extends T> Properties<P> storeTo(Properties<P> properties, V value);

    public abstract <P extends T> V retrieveFrom(Properties<P> properties);

    public abstract <A> void accumulate(V value, RekordSerializer.Serializer<A> serializer);

    public abstract Key<T, ?> original();

    @Override
    public String toString() {
        return name();
    }
}
