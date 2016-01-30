package com.noodlesandwich.rekord.serialization;

import java.util.Map;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.BuildableKey;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;

public final class MapDeserializer implements Deserializer<Map<String, Object>, KeyNotFoundException> {
    @Override
    public <T> Rekord<T> deserialize(Map<String, Object> serialized, Rekord<T> builder) throws KeyNotFoundException {
        Rekord<T> result = builder;
        for (Map.Entry<String, Object> entry : serialized.entrySet()) {
            Key<T, Object> key = result.keyNamed(entry.getKey());
            Object value = entry.getValue();
            if (key instanceof BuildableKey && value instanceof Map) {
                @SuppressWarnings("unchecked") Map<String, Object> innerSerialized =
                        (Map<String, Object>) value;
                @SuppressWarnings("unchecked") Rekord<?> innerBuilder =
                        ((BuildableKey<T, Rekord<?>>) (BuildableKey) key).builder();
                result = result.with(key, deserialize(innerSerialized, innerBuilder));
            } else {
                result = result.with(key, value);
            }
        }
        return result;
    }
}
