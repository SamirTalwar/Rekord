package com.noodlesandwich.rekord;

public interface Kollector<R> {
    Accumulator accumulator();

    R finish(Accumulator accumulator);

    public static interface Supplier<T> {
        T get();
    }

    public static interface Accumulator {
       <V> void accumulate(Key<?, V> key, V value);
    }

    public static interface Finisher<R> {
        R finish(Accumulator accumulator);
    }
}
