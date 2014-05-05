package com.noodlesandwich.rekord.serialization;

import static com.noodlesandwich.rekord.serialization.RekordSerializer.Accumulator;
import static com.noodlesandwich.rekord.serialization.RekordSerializer.Constructor;
import static com.noodlesandwich.rekord.serialization.RekordSerializer.Serializer;

public final class RekordSerializers {
    private RekordSerializers() { }

    public static <A> Serializer<A> serializer(Constructor<A> constructor, Accumulator<A> accumulator) {
        return new DelegatingSerializer<>(constructor, accumulator);
    }

    private static final class DelegatingSerializer<A> implements Serializer<A> {
        private final Constructor<A> constructor;
        private final Accumulator<A> accumulator;

        public DelegatingSerializer(Constructor<A> constructor, Accumulator<A> accumulator) {
            this.constructor = constructor;
            this.accumulator = accumulator;
        }

        @Override
        public RekordSerializer.SerializedProperty<A> newProperty(String name, Object value) {
            return constructor.newProperty(name, value);
        }

        @Override
        public Serializer<A> newCollection(String name) {
            return constructor.newCollection(name);
        }

        @Override
        public Serializer<A> newMap(String name) {
            return constructor.newMap(name);
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
