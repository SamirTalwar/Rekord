package com.noodlesandwich.rekord.serialization;

import java.util.Map;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;

public final class MapDeserializer implements Deserializer<Map<String, Object>, KeyNotFoundException> {
    @Override
    public <T> Rekord<T> deserialize(Map<String, Object> serialized, Rekord<T> builder) throws KeyNotFoundException {
        MapAccumulator<T> accumulator = new MapAccumulator<>(builder);
        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            Key<T, Object> key = builder.keyNamed(entry.getKey());
            Object value = entry.getValue();
            key.deserialize(value, accumulator, this);
        }
        return accumulator.result();
    }

    public static final class MapAccumulator<T> implements Accumulator<T, KeyNotFoundException> {
        private Rekord<T> builder;

        public MapAccumulator(Rekord<T> builder) {
            this.builder = builder;
        }

        @Override
        public <V> void addValue(Key<T, V> key, V value) {
            this.builder = builder.with(key, value);
        }

        @Override
        public Rekord<T> result() {
            return builder;
        }
    }
}
