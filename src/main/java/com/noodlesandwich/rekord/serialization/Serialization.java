package com.noodlesandwich.rekord.serialization;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.properties.Property;

public final class Serialization {
    private Serialization() { }

    public static <T> RekordSerialization<T> serialize(Rekord<T> rekord) {
        return new RekordSerialization<>(rekord);
    }

    public static final class RekordSerialization<T> {
        private final Rekord<T> rekord;

        public RekordSerialization(Rekord<T> rekord) {
            this.rekord = rekord;
        }

        public <A, E extends Exception> A into(Serializer.Accumulator<A, E> accumulator) throws E {
            for (Property<? super T, ?> property : rekord.properties()) {
                @SuppressWarnings("unchecked")
                Key<? super T, Object> castKey = (Key<? super T, Object>) property.key();
                Object value = property.value();
                castKey.accumulate(value, accumulator);
            }
            return accumulator.result();
        }
    }
}
