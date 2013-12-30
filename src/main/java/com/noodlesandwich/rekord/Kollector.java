package com.noodlesandwich.rekord;

public interface Kollector<A extends Kollector.Accumulator, R> {
    A accumulator();

    R finish(A accumulator);

    public static interface Accumulator {
       <V> void accumulate(Key<?, V> key, V value);
    }
}
