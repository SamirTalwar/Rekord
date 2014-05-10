package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.Serializer;
import org.pcollections.PSet;

public interface FixedRekord<T> extends RekordTemplate<T> {
    <V> V get(Key<? super T, V> key);

    boolean has(Key<T, ?> key);

    PSet<Key<? super T, ?>> keys();

    <R> R serialize(Serializer<R> serializer);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
