package com.noodlesandwich.rekord;

public interface Kollector<R> {
    Accumulator<R> accumulator();

    public static interface Accumulator<R> {
       <V> void accumulate(Key<?, V> key, V value);

        R finish();
    }
}
