package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;

public interface Deserializer<S, E extends Exception> {
    <T> Rekord<T> deserialize(S serialized, Rekord<T> builder) throws E;

    interface Accumulator<T, E extends Exception> {
        <V> void addValue(Key<T, V> key, V value) throws E;

        Rekord<T> result();
    }

    interface Accumulation<E extends Exception> {
        <T> void accumulateIn(Accumulator<T, E> accumulator) throws E;
    }
}
