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
        public void addCollection(String name, Accumulation accumulation) {
            MapCollectionAccumulator collectionAccumulator = new MapCollectionAccumulator();
            accumulation.accumulateIn(collectionAccumulator);
            result.put(name, collectionAccumulator.result());
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

    private static final class MapCollectionAccumulator implements Accumulator<Collection<Object>> {
        private final Collection<Object> result = new ArrayList<>();

        @Override
        public <V> void addValue(String name, V value) {
            result.add(value);
        }

        @Override
        public void addCollection(String name, Accumulation accumulation) {
            MapCollectionAccumulator collectionAccumulator = new MapCollectionAccumulator();
            accumulation.accumulateIn(collectionAccumulator);
            result.add(collectionAccumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation accumulation) {
            MapRekordAccumulator rekordAccumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(rekordAccumulator);
            result.add(rekordAccumulator.result());
        }

        @Override
        public Collection<Object> result() {
            return result;
        }
    }
}
