package com.noodlesandwich.rekord.serialization;

import static com.noodlesandwich.rekord.serialization.RekordSerializer.Accumulator;
import static com.noodlesandwich.rekord.serialization.RekordSerializer.Builder;
import static com.noodlesandwich.rekord.serialization.RekordSerializer.Serializer;

public final class RekordSerializers {
    private RekordSerializers() { }

    public static <A> Serializer<A> serializer(Builder<A> builder, Accumulator<A> accumulator) {
        return new DelegatingSerializer<>(builder, accumulator);
    }

    private static final class DelegatingSerializer<A> implements Serializer<A> {
        private final Builder<A> builder;
        private final Accumulator<A> accumulator;

        public DelegatingSerializer(Builder<A> builder, Accumulator<A> accumulator) {
            this.builder = builder;
            this.accumulator = accumulator;
        }

        @Override
        public RekordSerializer.SerializedProperty<A> single(String name, Object value) {
            return builder.single(name, value);
        }

        @Override
        public Serializer<A> collection(String name) {
            return builder.collection(name);
        }

        @Override
        public Serializer<A> map(String name) {
            return builder.map(name);
        }

        @Override
        public void accumulate(String name, RekordSerializer.SerializedProperty<A> property) {
            accumulator.accumulate(name, property);
        }

        @Override
        public A serialized() {
            return accumulator.serialized();
        }
    }
}
