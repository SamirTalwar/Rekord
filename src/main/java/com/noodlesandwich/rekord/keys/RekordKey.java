package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.serialization.Serialization;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class RekordKey<T, V> extends OriginalKey<T, Rekord<V>> implements BuildableKey<T, Rekord<V>> {
    private final Rekord<V> builder;

    private RekordKey(String name, Rekord<V> builder) {
        super(name);
        this.builder = builder;
    }

    public static UnbuildableRekordKey named(String name) {
        return new UnbuildableRekordKey(name);
    }

    @Override
    public Rekord<V> builder() {
        return builder;
    }

    @Override
    public <A, E extends Exception> void accumulate(final Rekord<V> rekord, Serializer.Accumulator<A, E> accumulator) throws E {
        accumulator.addRekord(name(), rekord.name(), new Serializer.Accumulation() {
            @Override
            public <A2, E2 extends Exception> void accumulateIn(Serializer.Accumulator<A2, E2> mapAccumulator) throws E2 {
                Serialization.serialize(rekord).into(mapAccumulator);
            }
        });
    }

    public static final class UnbuildableRekordKey {
        private final String name;

        public UnbuildableRekordKey(String name) {
            this.name = name;
        }

        public <T, V> RekordKey<T, V> builtFrom(Rekord<V> builder) {
            return new RekordKey<>(name, builder);
        }
    }
}
