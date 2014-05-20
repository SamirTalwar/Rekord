package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.Serializer;
import org.pcollections.PSet;

public interface FixedRekord<T> extends RekordTemplate<T> {
    <V> V get(Key<? super T, V> key);

    boolean has(Key<? super T, ?> key);

    PSet<Key<? super T, ?>> keys();

    <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E;

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
