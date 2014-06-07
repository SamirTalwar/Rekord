package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Named;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;

public interface Key<T, V> extends Keys<T>, Named {
    <R extends T> boolean test(Properties<R> properties);

    <R extends T> V get(Properties<R> properties);

    <R extends T> Properties<R> set(V value, Properties<R> properties);

    <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E;
}
