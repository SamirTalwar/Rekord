package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public interface Serializer<S, E extends Exception> {
    <T> S serialize(String name, FixedRekord<T> rekord) throws E;

    interface Accumulator<A, E extends Exception> {
        void addValue(String name, Object value) throws E;
        void addIterable(String name, Accumulation<E> accumulation) throws E;
        void addRekord(String name, String rekordName, Accumulation<E> accumulation) throws E;
        A result() throws E;
    }

    interface Accumulation<E extends Exception> {
        <A> void accumulateIn(Accumulator<A, E> accumulator) throws E;
    }
}
