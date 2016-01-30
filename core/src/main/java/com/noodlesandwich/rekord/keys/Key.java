package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Named;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;

public interface Key<T, V> extends Keys<T>, Named {
    boolean test(Properties<T> properties);

    V get(Properties<T> properties);

    Properties<T> set(V value, Properties<T> properties);

    <A, E extends Exception> void serialize(V value, Serializer.Accumulator<A, E> accumulator) throws E;

    <S, E extends Exception> void deserialize(Object value, Deserializer.Accumulator<T, E> accumulator, Deserializer<S, E> deserializer) throws E;
}
