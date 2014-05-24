package com.noodlesandwich.rekord.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;

public final class MapSerializer implements SafeSerializer<Map<String, Object>> {
    @Override
    public <T> Map<String, Object> serialize(Key<?, FixedRekord<T>> key, FixedRekord<T> rekord) {
        return Serialization.serialize(rekord).into(new MapRekordAccumulator());
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
