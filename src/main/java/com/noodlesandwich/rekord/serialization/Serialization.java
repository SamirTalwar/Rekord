package com.noodlesandwich.rekord.serialization;

import java.util.Collection;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;

public final class Serialization {
    private Serialization() { }

    public static <V> CollectionSerializationBuilder<V> serialize(Collection<V> collection) {
        return new CollectionSerializationBuilder<>(collection);
    }

    public static <T> RekordSerialization<T> serialize(FixedRekord<T> rekord) {
        return new RekordSerialization<>(rekord);
    }

    public static final class CollectionSerializationBuilder<V> {
        private final Collection<V> collection;

        public CollectionSerializationBuilder(Collection<V> collection) {
            this.collection = collection;
        }

        public <T> CollectionSerialization<T, V> with(Key<T, V> key) {
            return new CollectionSerialization<>(collection, key);
        }
    }

    public static final class CollectionSerialization<T, V> {
        private final Collection<V> collection;
        private final Key<T, V> key;

        public CollectionSerialization(Collection<V> collection, Key<T, V> key) {
            this.collection = collection;
            this.key = key;
        }

        public <A> A into(Serializer.Accumulator<A> accumulator) {
            for (V value : collection) {
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

        public <A> A into(Serializer.Accumulator<A> accumulator) {
            for (Key<? super T, ?> key : rekord.keys()) {
                @SuppressWarnings("unchecked")
                Key<? super T, Object> castKey = (Key<? super T, Object>) key;
                Object value = rekord.get(castKey);
                castKey.accumulate(value, accumulator);
            }
            return accumulator.result();
        }
    }
}
