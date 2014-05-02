package com.noodlesandwich.rekord;

public interface Kollector<A extends Kollector.Accumulator<R>, R> {
    A accumulatorNamed(String name);

    public static interface Accumulator<R> {
        <V> void accumulate(Key<?, V> key, V value);
        void accumulateRekord(Key<?, ?> key, R kollectedRekord);
        R finish();
    }
}
