package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.serialization.Serialization;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class RekordKey<T, V> extends AbstractKey<T, FixedRekord<V>> {
    private RekordKey(String name) {
        super(name);
    }

    public static <T, R> RekordKey<T, R> named(String name) {
        return new RekordKey<>(name);
    }

    @Override
    public <A, E extends Exception> void accumulate(final FixedRekord<V> rekord, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addRekord(name(), rekord.name(), new Serializer.Accumulation() {
            @Override
            public <A2, E2 extends Exception> void accumulateIn(Serializer.Accumulator<A2, E2> mapAccumulator) throws E2 {
                Serialization.serialize(rekord).into(mapAccumulator);
            }
        });
    }
}
