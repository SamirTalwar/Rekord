package com.noodlesandwich.rekord.serialization;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.noodlesandwich.rekord.FixedRekord;

public final class MapSerializer implements
        SafeSerializer<Map<String, Object>> {
    @Override
    public <T> Map<String, Object> serialize(String name, FixedRekord<T> rekord) {
        return Serialization.serialize(rekord).into(new MapRekordAccumulator());
    }

    private static final class MapRekordAccumulator implements SafeAccumulator<Map<String, Object>> {
        private final Map<String, Object> result = new HashMap<>();

        @Override
        public void addValue(String name, Object value) {
            result.put(name, value);
        }

        @Override
        public void addCollection(String name, Accumulation<ImpossibleException> accumulation) {
            MapCollectionAccumulator accumulator = new MapCollectionAccumulator();
            accumulation.accumulateIn(accumulator);
            result.put(name, accumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation<ImpossibleException> accumulation) {
            MapRekordAccumulator accumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(accumulator);
            result.put(name, accumulator.result());
        }

        @Override
        public Map<String, Object> result() {
            return result;
        }
    }

    private static final class MapCollectionAccumulator implements SafeAccumulator<Collection<Object>> {
        private final Collection<Object> result = new ArrayList<>();

        @Override
        public void addValue(String name, Object value) {
            result.add(value);
        }

        @Override
        public void addCollection(String name, Accumulation<ImpossibleException> accumulation) {
            MapCollectionAccumulator accumulator = new MapCollectionAccumulator();
            accumulation.accumulateIn(accumulator);
            result.add(accumulator.result());
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation<ImpossibleException> accumulation) {
            MapRekordAccumulator accumulator = new MapRekordAccumulator();
            accumulation.accumulateIn(accumulator);
            result.add(accumulator.result());
        }

        @Override
        public Collection<Object> result() {
            return result;
        }
    }
}
