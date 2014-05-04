package com.noodlesandwich.rekord.serialization;

public interface Serializer<A, R> {
    Accumulator<A> start(String name);
    R finish(Accumulator<A> accumulator);

    public static interface Accumulator<A> {
        void accumulate(String name, Object value);
        void accumulateNested(String name, Accumulator<A> accumulator);
        Accumulator<A> nest(String name);
        A value();
    }
}
