package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.serialization.RekordSerializer;
import org.pcollections.PSet;

public interface FixedRekord<T> extends RekordTemplate<T> {
    <V> V get(Key<? super T, V> key);

    boolean has(Key<T, ?> key);

    PSet<Key<? super T, ?>> keys();

    <A, R> R serialize(RekordSerializer<A, R> serializer);

    <A> void accumulateIn(RekordSerializer.Serializer<A> serializer);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
