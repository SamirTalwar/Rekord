package com.noodlesandwich.rekord;

public interface Kollector<T extends RekordType, R> {
    Accumulator<T> accumulator();

    R finish(Accumulator<T> accumulator);

    public static interface Supplier<T> {
        T get();
    }

    public static interface Accumulator<T> {
       <V> void accumulate(Key<? super T, V> key, V value);
    }

    public static interface Finisher<A, R> {
        R finish(A accumulator);
    }
}
