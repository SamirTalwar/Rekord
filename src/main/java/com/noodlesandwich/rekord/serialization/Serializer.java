package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;

public interface Serializer<R> {
    <T> R serialize(FixedRekord<T> rekord);

    public static interface Accumulator<A> {
        <V> void addValue(String name, V value);
        void addCollection(String name, Accumulation accumulation);
        void addRekord(String name, String rekordName, Accumulation accumulation);
        A result();
    }

    public static interface Accumulation {
        <A> void accumulateIn(Accumulator<A> accumulator);
    }
}
