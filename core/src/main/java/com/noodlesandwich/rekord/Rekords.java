package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.LimitedRekord;
import com.noodlesandwich.rekord.implementation.PersistentProperties;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.serialization.Deserializer;

public final class Rekords {
    private Rekords() { }

    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        private UnkeyedRekord(String name) {
            this.name = name;
        }

        // CHECKSTYLE:OFF
        @SuppressWarnings("varargs")
        @SafeVarargs
        public final Rekord<T> accepting(Keys<T>... keys) {
            return accepting(KeySet.from(keys));
        }
        // CHECKSTYLE:ON

        public Rekord<T> accepting(Keys<T> keys) {
            return new LimitedRekord<>(name, keys, new PersistentProperties<T>());
        }
    }

    public static <S> UnstructuredDeserializer<S> deserialize(S serialized) {
        return new UnstructuredDeserializer<>(serialized);
    }

    public static final class UnstructuredDeserializer<S> {
        private final S serialized;

        public UnstructuredDeserializer(S serialized) {
            this.serialized = serialized;
        }

        public <T> UnmethodicalDeserializer<S, T> into(Rekord<T> rekordBuilder) {
            return new UnmethodicalDeserializer<>(serialized, rekordBuilder);
        }
    }

    public static final class UnmethodicalDeserializer<S, T> {
        private final S serialized;
        private final Rekord<T> rekordBuilder;

        public UnmethodicalDeserializer(S serialized, Rekord<T> rekordBuilder) {
            this.serialized = serialized;
            this.rekordBuilder = rekordBuilder;
        }

        public <E extends Exception> Rekord<T> with(Deserializer<S, E> deserializer) throws E {
            return deserializer.deserialize(serialized, rekordBuilder);
        }
    }
}
