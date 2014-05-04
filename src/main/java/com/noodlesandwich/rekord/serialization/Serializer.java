package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Key;

public interface Serializer<R> {
    Accumulator<R> accumulatorNamed(String name);

    public static interface Accumulator<R> {
        <V> void accumulate(Key<?, V> key, V value);
        void accumulateRekord(Key<?, ?> key, R serializedRekord);
        R finish();
    }
}
