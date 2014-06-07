package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;

public interface Serializer<R, E extends Exception> {
    <T> R serialize(Key<?, Rekord<T>> key, Rekord<T> rekord) throws E;

    public interface Accumulator<A, E extends Exception> {
        void addValue(String name, Object value) throws E;
        void addIterable(String name, Accumulation accumulation) throws E;
        void addRekord(String name, String rekordName, Accumulation accumulation) throws E;
        A result() throws E;
    }

    public interface Accumulation {
        <A, E extends Exception> void accumulateIn(Accumulator<A, E> accumulator) throws E;
    }
}
