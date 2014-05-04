package com.noodlesandwich.rekord.serialization;

public interface Serializer<A, R> {
    Accumulator<A, R> nest(String name);

    public static interface Accumulator<A, R> extends Serializer<A, R> {
        <V> void accumulate(String name, V value);
        void accumulateNested(String name, Accumulator<A, R> value);
        A value();
        R finish();
    }
}
