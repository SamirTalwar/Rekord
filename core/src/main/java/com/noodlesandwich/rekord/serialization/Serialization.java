package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.properties.Property;

public final class Serialization {
    private Serialization() { }

    public static <T> RekordSerialization<T> serialize(FixedRekord<T> rekord) {
        return new RekordSerialization<>(rekord);
    }

    public static final class RekordSerialization<T> {
        private final FixedRekord<T> rekord;

        public RekordSerialization(FixedRekord<T> rekord) {
            this.rekord = rekord;
        }

        public <A, E extends Exception> A into(Serializer.Accumulator<A, E> accumulator) throws E {
            for (Property<T, ?> property : rekord.properties()) {
                @SuppressWarnings("unchecked")
                Key<T, Object> castKey = (Key<T, Object>) property.key();
                Object value = property.value();
                castKey.serialize(value, accumulator);
            }
            return accumulator.result();
        }
    }
}
