package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.RekordBuilder;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class ValidRekord {
    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    public static <T, B extends RekordBuilder<T, B>> UnsureRekord<T> validating(B rekord) {
        return ValidRekord.<T>create(rekord.name()).accepting(rekord.acceptedKeys());
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        public UnkeyedRekord(String name) {
            this.name = name;
        }

        @SafeVarargs
        public final UnsureRekord<T> accepting(Key<? super T, ?>... keys) {
            return accepting(OrderedPSet.from(Arrays.asList(keys)));
        }

        @SuppressWarnings("unchecked")
        public final UnsureRekord<T> accepting(PSet<Key<? super T, ?>> keys) {
            PSet<Key<?, ?>> untypedKeys = (PSet<Key<?, ?>>) (PSet) keys;
            return new UnsureRekord<>(name, new Properties(untypedKeys));
        }
    }

    public static final class UnsureRekord<T> {
        private final String name;
        private final Properties properties;

        public UnsureRekord(String name, Properties properties) {
            this.name = name;
            this.properties = properties;
        }

        public ValidatingRekord<T> allowing(Validator<T> validator) {
            return new ValidatingRekord<>(name, properties, validator);
        }
    }
}
