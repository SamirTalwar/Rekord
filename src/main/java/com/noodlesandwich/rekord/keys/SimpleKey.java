package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class SimpleKey<T, V> extends AbstractKey<T, V> {
    private SimpleKey(String name) {
        super(name);
    }

    public static <T, V> Key<T, V> named(String name) {
        return new SimpleKey<>(name);
    }

    @Override
    public <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addValue(name(), value);
    }
}
