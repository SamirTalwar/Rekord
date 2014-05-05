package com.noodlesandwich.rekord;

import java.util.Set;
import com.noodlesandwich.rekord.serialization.RekordSerializer;

public interface FixedRekord<T> {
    String name();

    <V> V get(Key<? super T, V> key);

    boolean containsKey(Key<T, ?> key);

    Set<Key<? super T, ?>> keys();

    <A, R> R serialize(RekordSerializer<A, R> serializer);

    <A> void accumulateIn(RekordSerializer.Serializer<A> serializer);

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    @Override
    String toString();
}
