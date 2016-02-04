package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class SimpleKey<T, V> extends OriginalKey<T, V> {
    private SimpleKey(String name) {
        super(name);
    }

    public static <T, V> Key<T, V> named(String name) {
        return new SimpleKey<>(name);
    }

    @Override
    public <A, E extends Exception> void serialize(V value, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addValue(name(), value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R, E extends Exception> void deserialize(Object value, Deserializer.Accumulator<T, R, E> accumulator) throws E {
        accumulator.addValue(this, (V) value);
    }
}
