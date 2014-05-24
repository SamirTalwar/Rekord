package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.properties.Property;

public final class Serialization {
    private Serialization() { }

    public static <V> IterableSerializationBuilder<V> serialize(Iterable<V> iterable) {
        return new IterableSerializationBuilder<>(iterable);
    }

    public static <T> RekordSerialization<T> serialize(FixedRekord<T> rekord) {
        return new RekordSerialization<>(rekord);
    }

    public static final class IterableSerializationBuilder<V> {
        private final Iterable<V> iterable;

        public IterableSerializationBuilder(Iterable<V> iterable) {
            this.iterable = iterable;
        }

        public <T> IterableSerialization<T, V> with(Key<T, V> key) {
            return new IterableSerialization<>(iterable, key);
        }
    }

    public static final class IterableSerialization<T, V> {
        private final Iterable<V> iterable;
        private final Key<T, V> key;

        public IterableSerialization(Iterable<V> iterable, Key<T, V> key) {
            this.iterable = iterable;
            this.key = key;
        }

        public <A, E extends Exception> A into(Serializer.Accumulator<A, E> accumulator) throws E {
            for (V value : iterable) {
                key.accumulate(value, accumulator);
            }
            return accumulator.result();
        }
    }

    public static final class RekordSerialization<T> {
        private final FixedRekord<T> rekord;

        public RekordSerialization(FixedRekord<T> rekord) {
            this.rekord = rekord;
        }

        public <A, E extends Exception> A into(Serializer.Accumulator<A, E> accumulator) throws E {
            for (Property<? super T, ?> property : rekord.properties()) {
                @SuppressWarnings("unchecked")
                Key<? super T, Object> castKey = (Key<? super T, Object>) property.key();
                Object value = property.value();
                castKey.accumulate(value, accumulator);
            }
            return accumulator.result();
        }
    }
}
