package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.LimitedRekord;
import com.noodlesandwich.rekord.implementation.PersistentProperties;
import com.noodlesandwich.rekord.keys.Keys;

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
        public final Rekord<T> accepting(Keys<? super T>... keys) {
            return accepting(KeySet.from(keys));
        }
        // CHECKSTYLE:ON

        public Rekord<T> accepting(Keys<T> keys) {
            return new LimitedRekord<>(name, keys, new PersistentProperties<T>());
        }
    }
}
