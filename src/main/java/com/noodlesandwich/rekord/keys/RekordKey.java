package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.serialization.Serialization;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class RekordKey<T, V> extends OriginalKey<T, FixedRekord<V>> {
    private RekordKey(String name) {
        super(name);
    }

    public static <T, R> Key<T, FixedRekord<R>> named(String name) {
        return new RekordKey<>(name);
    }

    @Override
    public <A> void accumulate(final FixedRekord<V> rekord, Serializer.Accumulator<A> accumulator) {
        Serialization.serialize(name(), rekord).into(accumulator);
    }
}
