package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Named;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.transformers.Transformer;

public interface Key<T, V> extends Keys<T>, Named {
    Property<T, ?> of(V value);

    <P extends T> V get(Properties<P> properties);

    <NewV> Key<T, NewV> that(Transformer<V, NewV> transformer);

    <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer);

    <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E;

    Key<T, ?> original();
}
