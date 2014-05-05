package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.serialization.RekordSerializer;

public final class RekordKey<T, V> extends OriginalKey<T, FixedRekord<V>> {
    private RekordKey(String name) {
        super(name);
    }

    public static <T, R> Key<T, FixedRekord<R>> named(String name) {
        return new RekordKey<>(name);
    }

    @Override
    public <A> void accumulate(FixedRekord<V> value, RekordSerializer.Serializer<A> serializer) {
        RekordSerializer.Serializer<A> mapSerializer = serializer.newMap(value.name());
        value.accumulateIn(mapSerializer);
        serializer.accumulate(name(), mapSerializer);
    }
}
