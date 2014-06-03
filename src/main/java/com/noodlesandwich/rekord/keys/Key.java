package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Named;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;

public interface Key<T, V> extends Keys<T>, Named {
    <R extends T> boolean test(PropertyMap<R> properties);

    <R extends T> V get(PropertyMap<R> properties);

    <R extends T> PropertyMap<R> set(V value, PropertyMap<R> properties);

    <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E;
}
