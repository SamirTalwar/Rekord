package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public interface Serializer<R, E extends Exception> {
    <T> R serialize(FixedRekord<T> rekord) throws E;

    public static interface Accumulator<A, E extends Exception> {
        <V> void addValue(String name, V value) throws E;
        void addIterable(String name, Accumulation accumulation) throws E;
        void addRekord(String name, String rekordName, Accumulation accumulation) throws E;
        A result() throws E;
    }

    public static interface Accumulation {
        <A, E extends Exception> void accumulateIn(Accumulator<A, E> accumulator) throws E;
    }
}
