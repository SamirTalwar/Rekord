package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeySet;
import com.noodlesandwich.rekord.serialization.Serializer;

public interface FixedRekord<T> extends RekordTemplate<T> {
    <V> V get(Key<? super T, V> key);

    boolean has(Key<? super T, ?> key);

    KeySet<T> keys();

    <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E;

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
