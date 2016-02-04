package com.noodlesandwich.rekord.serialization;

import java.util.Collection;
import java.util.Map;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.BuildableKey;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;

public final class MapDeserializer implements Deserializer<Map<String, Object>, KeyNotFoundException> {
    @Override
    public <T> Rekord<T> deserialize(Map<String, Object> serialized, Rekord<T> builder) throws KeyNotFoundException {
        MapRekordAccumulator<T> accumulator = new MapRekordAccumulator<>(this, builder);
        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            Key<T, Object> key = builder.keyNamed(entry.getKey());
            Object value = entry.getValue();
            key.deserialize(value, accumulator);
        }
        return accumulator.result();
    }

    public static final class MapRekordAccumulator<T> implements Accumulator<T, Rekord<T>, KeyNotFoundException> {
        private final MapDeserializer deserializer;
        private Rekord<T> builder;

        public MapRekordAccumulator(MapDeserializer deserializer, Rekord<T> builder) {
            this.deserializer = deserializer;
            this.builder = builder;
        }

        @Override
        public <V> void addValue(Key<T, V> key, V value) {
            this.builder = builder.with(key, value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <V, C extends Collection<V>> void addCollection(BuildableKey<T, C> key, Key<T, V> contents, Object serializedCollection) throws KeyNotFoundException {
            MapCollectionAccumulator<T, V, C> accumulator = new MapCollectionAccumulator<>(deserializer, key.builder());
            for (Object value : (Iterable<Object>) serializedCollection) {
                contents.deserialize(value, accumulator);
            }
            this.builder = builder.with(key, accumulator.result());
        }

        @Override
        public <V> void addRekord(BuildableKey<T, Rekord<V>> key, Object serializedRekord, Rekord<V> rekordBuilder) throws KeyNotFoundException {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) serializedRekord;
            Rekord<V> value = deserializer.deserialize(map, rekordBuilder);
            this.builder = builder.with(key, value);
        }

        @Override
        public Rekord<T> result() {
            return builder;
        }
    }

    public static final class MapCollectionAccumulator<T, V, C extends Collection<V>> implements Accumulator<T, C, KeyNotFoundException> {
        private final MapDeserializer deserializer;
        private final C builder;

        public MapCollectionAccumulator(MapDeserializer deserializer, C builder) {
            this.deserializer = deserializer;
            this.builder = builder;
        }

        @SuppressWarnings("unchecked")
        @Override
        public <V2> void addValue(Key<T, V2> key, V2 value) throws KeyNotFoundException {
            this.builder.add((V) value);
        }

        @SuppressWarnings("unchecked")
        @Override
        public <V2, C2 extends Collection<V2>> void addCollection(BuildableKey<T, C2> key, Key<T, V2> contents, Object serializedCollection) throws KeyNotFoundException {
            MapCollectionAccumulator<T, V2, C2> accumulator = new MapCollectionAccumulator<>(deserializer, key.builder());
            for (Object value : (Iterable<Object>) serializedCollection) {
                contents.deserialize(value, accumulator);
            }
            this.builder.add((V) accumulator.result());
        }

        @SuppressWarnings("unchecked")
        @Override
        public <V2> void addRekord(BuildableKey<T, Rekord<V2>> key, Object serializedRekord, Rekord<V2> rekordBuilder) throws KeyNotFoundException {
            Map<String, Object> map = (Map<String, Object>) serializedRekord;
            V value = (V) deserializer.deserialize(map, key.builder());
            this.builder.add(value);
        }

        @Override
        public C result() {
            return builder;
        }
    }
}
