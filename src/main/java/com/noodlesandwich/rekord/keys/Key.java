package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Named;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.serialization.Serializer;

public interface Key<T, V> extends Keys<T>, Named {
    Property<T, ?> of(V value);

    <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E;
}
