package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;

public interface FixedRekord<T> extends RekordTemplate<T> {
    boolean has(Key<? super T, ?> key);

    <V> V get(Key<? super T, V> key);

    Keys<T> keys();

    Properties<T> properties();

    <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E;

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
