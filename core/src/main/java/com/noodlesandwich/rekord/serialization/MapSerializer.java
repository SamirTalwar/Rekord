package com.noodlesandwich.rekord.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;

public final class MapSerializer implements SafeSerializer<Map<String, Object>>, SafeDeserializer<Map<String, Object>> {
    @Override
    public <T> Map<String, Object> serialize(String name, FixedRekord<T> rekord) {
        return Serialization.serialize(rekord).into(new MapRekordAccumulator());
    }

    @Override
    public <T> Rekord<T> deserialize(Map<String, Object> serialized, Rekord<T> builder) {
        Rekord<T> result = builder;
        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            Key<T, Object> key = result.keyNamed(entry.getKey());
            Object value = entry.getValue();
            result = result.with(key, value);
        }
        return result;
    }

    private static final class MapRekordAccumulator implements SafeAccumulator<Map<String, Object>> {
        private final Map<String, Object> result = new HashMap<>();

        @Override
        public void addValue(String name, Object value) {
            result.put(name, value);
        }

        @Override
        public void addIterable(String name, Accumulation accumulation) {
            MapIterableAccumulator iterableAccumulator = new MapIterableAccumulator();
            accumulation.accumulateIn(iterableAccumulator);
            result.put(name, iterableAccumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) {
            MapRekordAccumulator rekordAccumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(rekordAccumulator);
            result.put(name, rekordAccumulator.result());
        }

        @Override
        public Map<String, Object> result() {
            return result;
        }
    }

    private static final class MapIterableAccumulator implements SafeAccumulator<Iterable<Object>> {
        private final Collection<Object> result = new ArrayList<>();

        @Override
        public void addValue(String name, Object value) {
            result.add(value);
        }

        @Override
        public void addIterable(String name, Accumulation accumulation) {
            MapIterableAccumulator iterableAccumulator = new MapIterableAccumulator();
            accumulation.accumulateIn(iterableAccumulator);
            result.add(iterableAccumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) {
            MapRekordAccumulator rekordAccumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(rekordAccumulator);
            result.add(rekordAccumulator.result());
        }

        @Override
        public Iterable<Object> result() {
            return result;
        }
    }
}
