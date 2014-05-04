package com.noodlesandwich.rekord;

public interface Kollector<R> {
    Accumulator<R> accumulatorNamed(String name);

    public static interface Accumulator<R> {
        <V> void accumulate(Key<?, V> key, V value);
        void accumulateRekord(Key<?, ?> key, R kollectedRekord);
        R finish();
    }
}
