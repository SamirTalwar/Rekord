package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class RekordKey<T, V> extends OriginalKey<T, Rekord<V>> {
    private RekordKey(String name) {
        super(name);
    }

    public static <T, R> Key<T, Rekord<R>> named(String name) {
        return new RekordKey<>(name);
    }

    @Override
    public <A> void accumulate(Rekord<V> value, Serializer.Accumulator<A> accumulator) {
        Serializer.Accumulator<A> nested = accumulator.nest(value.name());
        value.accumulateIn(nested);
        accumulator.accumulate(name(), nested);
    }
}
