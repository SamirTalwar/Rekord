package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Key;

public interface Serializer<A, R> {
    Accumulator<A, R> accumulatorNamed(String name);

    public static interface Accumulator<A, R> {
        <V> void accumulate(Key<?, V> key, V value);
        void accumulateNested(Key<?, ?> key, Accumulator<A, R> value);
        Accumulator<A, R> nest(String name);
        A value();
        R finish();
    }
}
