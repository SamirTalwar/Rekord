package com.noodlesandwich.rekord.serialization;

import static com.noodlesandwich.rekord.serialization.Serializer.Accumulator;
import static com.noodlesandwich.rekord.serialization.Serializer.AccumulatorBuilder;
import static com.noodlesandwich.rekord.serialization.Serializer.Builder;

public final class Serializers {
    private Serializers() { }

    public static <A> AccumulatorBuilder<A> accumulatorBuilder(Builder<A> builder, Accumulator<A> accumulator) {
        return new JoinedAccumulatorBuilder<>(builder, accumulator);
    }

    private static final class JoinedAccumulatorBuilder<A> implements AccumulatorBuilder<A> {
        private final Builder<A> builder;
        private final Accumulator<A> accumulator;

        public JoinedAccumulatorBuilder(Builder<A> builder, Accumulator<A> accumulator) {
            this.builder = builder;
            this.accumulator = accumulator;
        }

        @Override
        public Serializer.SerializedProperty<A> single(String name, Object value) {
            return builder.single(name, value);
        }

        @Override
        public AccumulatorBuilder<A> collection(String name) {
            return builder.collection(name);
        }

        @Override
        public AccumulatorBuilder<A> nest(String name) {
            return builder.nest(name);
        }

        @Override
        public void accumulate(String name, Serializer.SerializedProperty<A> property) {
            accumulator.accumulate(name, property);
        }

        @Override
        public A serialized() {
            return accumulator.serialized();
        }
    }
}
