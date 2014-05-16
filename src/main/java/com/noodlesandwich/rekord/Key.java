package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.transformers.Transformer;

public interface Key<T, V> extends Named {
    <P extends T> Properties<P> storeTo(Properties<P> properties, V value);

    <P extends T> V retrieveFrom(Properties<P> properties);

    <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer);

    <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer);

    <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E;

    Key<T, ?> original();
}
