package com.noodlesandwich.rekord.serialization;

import java.util.HashMap;
import java.util.Map;

import static com.noodlesandwich.rekord.serialization.RekordSerializers.serializer;

public final class MapSerializer implements RekordSerializer<Map<String, Object>, Map<String, Object>> {
    @Override
    public Serializer<Map<String, Object>> start(String name) {
        return new MapConstructor().newMap(name);
    }

    @Override
    public Map<String, Object> finish(Serializer<Map<String, Object>> serializer) {
        return serializer.serialized();
    }

    private static final class MapConstructor implements Constructor<Map<String, Object>> {
        @Override
        public SerializedProperty<Map<String, Object>> newProperty(String name, Object value) {
            return new Entry(name, value);
        }

        @Override
        public Serializer<Map<String, Object>> newCollection(String name) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Serializer<Map<String, Object>> newMap(String name) {
            return serializer(this, new MapAccumulator());
        }

        private static final class Entry implements SerializedProperty<Map<String, Object>> {
            private final Map<String, Object> result;

            public Entry(String name, Object value) {
                this.result = new HashMap<>();
                result.put(name, value);
            }

            @Override
            public Map<String, Object> serialized() {
                return result;
            }
        }
    }

    private static final class MapAccumulator implements Accumulator<Map<String,Object>> {
        private final Map<String, Object> result = new HashMap<>();

        @Override
        public void accumulate(String name, SerializedProperty<Map<String, Object>> property) {
            result.putAll(property.serialized());
        }

        @Override
        public Map<String, Object> serialized() {
            return result;
        }
    }
}
