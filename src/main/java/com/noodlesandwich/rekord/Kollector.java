package com.noodlesandwich.rekord;

public interface Kollector<A extends Kollector.Accumulator<R>, R> {
    A accumulatorNamed(String name);

    R finish(A accumulator);

    public static interface Accumulator<R> {
        <V> void accumulate(Key<?, V> key, V value);
        void accumulateRekord(Key<?, ?> key, R kollectedRekord);
    }
}
