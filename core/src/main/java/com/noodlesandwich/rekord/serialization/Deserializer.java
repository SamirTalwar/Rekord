package com.noodlesandwich.rekord.serialization;

import java.util.Collection;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.BuildableKey;
import com.noodlesandwich.rekord.keys.Key;

public interface Deserializer<S, E extends Exception> {
    <T> Rekord<T> deserialize(S serialized, Rekord<T> builder) throws E;

    interface Accumulator<T, R, E extends Exception> {
        <V> void addValue(Key<T, V> key, V value) throws E;

        <V, C extends Collection<V>> void addCollection(BuildableKey<T, C> key, Key<T, V> contents, Object serializedCollection) throws E;

        <V> void addRekord(BuildableKey<T, Rekord<V>> key, Object serializedRekord, Rekord<V> rekordBuilder) throws E;

        R result();
    }
}
