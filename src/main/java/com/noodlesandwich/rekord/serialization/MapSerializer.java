package com.noodlesandwich.rekord.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.noodlesandwich.rekord.FixedRekord;

public final class MapSerializer implements Serializer<Map<String, Object>> {
    @Override
    public <T> Map<String, Object> serialize(FixedRekord<T> rekord) {
        return Serialization.serialize(rekord).into(new MapRekordAccumulator());
    }

    private static final class MapRekordAccumulator implements Accumulator<Map<String, Object>> {
        private final Map<String, Object> result = new HashMap<>();

        @Override
        public <V> void addValue(String name, V value) {
            result.put(name, value);
        }

        @Override
        public void addCollection(String name, Accumulation<Map<String, Object>> accumulation) {
            MapCollectionAccumulator collectionAccumulator = new MapCollectionAccumulator(name);
            accumulation.accumulateIn(collectionAccumulator);
            result.putAll(collectionAccumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation<Map<String, Object>> accumulation) {
            MapRekordAccumulator rekordAccumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(rekordAccumulator);
            result.put(name, rekordAccumulator.result());
        }

        @Override
        public Map<String, Object> result() {
            return result;
        }
    }

    private static final class MapCollectionAccumulator implements Accumulator<Map<String, Object>> {
        private final String name;
        private final Collection<Object> result = new ArrayList<>();

        public MapCollectionAccumulator(String name) {
            this.name = name;
        }

        @Override
        public <V> void addValue(String name, V value) {
            result.add(value);
        }

        @Override
        public void addCollection(String name, Accumulation<Map<String, Object>> accumulation) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation<Map<String, Object>> accumulation) {
            MapRekordAccumulator rekordAccumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(rekordAccumulator);
            result.add(rekordAccumulator.result());
        }

        @Override
        public Map<String, Object> result() {
            return container(name, result);
        }
    }

    private static Map<String, Object> container(String name, Object value) {
        Map<String, Object> container = new HashMap<>();
        container.put(name, value);
        return container;
    }
}
