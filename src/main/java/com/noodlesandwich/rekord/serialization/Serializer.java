package com.noodlesandwich.rekord.serialization;

public interface Serializer<A, R> {
    Accumulator<A, R> nest(String name);

    public static interface Accumulator<A, R> extends Serializer<A, R> {
        void accumulate(String name, Object value);
        void accumulateNested(String name, Accumulator<A, R> accumulator);
        A value();
        R finish();
    }
}
