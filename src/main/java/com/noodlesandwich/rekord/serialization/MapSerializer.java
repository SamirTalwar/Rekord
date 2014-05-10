package com.noodlesandwich.rekord.serialization;

import java.util.HashMap;
import java.util.Map;
import com.noodlesandwich.rekord.FixedRekord;

public final class MapSerializer implements Serializer<Map<String, Object>> {
    @Override
    public <T> Map<String, Object> serialize(FixedRekord<T> rekord) {
        return Serialization.serialize(rekord).into(new MapAccumulator());
    }

    private static final class MapAccumulator implements Accumulator<Map<String,Object>> {
        private final Map<String, Object> result = new HashMap<>();

        @Override
        public <V> void addValue(String name, V value) {
            result.put(name, value);
        }

        @Override
        public void addCollection(String name, Accumulation<Map<String, Object>> accumulation) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void addRekord(String name, String rekordName, Accumulation<Map<String, Object>> accumulation) {
            MapAccumulator rekordAccumulator = new MapAccumulator();
            accumulation.accumulateIn(rekordAccumulator);
            result.put(name, rekordAccumulator.result());
        }

        @Override
        public Map<String, Object> result() {
            return result;
        }
    }
}
