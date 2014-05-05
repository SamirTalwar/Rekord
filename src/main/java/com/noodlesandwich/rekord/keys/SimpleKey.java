package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class SimpleKey<T, V> extends OriginalKey<T, V> {
    private SimpleKey(String name) {
        super(name);
    }

    public static <T, V> Key<T, V> named(String name) {
        return new SimpleKey<>(name);
    }

    @Override
    public <A> void accumulate(V value, Serializer.AccumulatorBuilder<A> accumulator) {
        accumulator.accumulate(name(), accumulator.single(name(), value));
    }
}
