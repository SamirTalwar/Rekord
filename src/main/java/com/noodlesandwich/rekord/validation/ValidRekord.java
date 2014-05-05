package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class ValidRekord {
    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        public UnkeyedRekord(String name) {
            this.name = name;
        }

        @SafeVarargs
        public final UnsureRekord<T> accepting(Key<? super T, ?>... keys) {
            return accepting(OrderedPSet.from(Arrays.<Key<?, ?>>asList(keys)));
        }

        public final UnsureRekord<T> accepting(PSet<Key<?, ?>> keys) {
            return new UnsureRekord<>(name, new Properties(keys));
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

    public static interface Validator<T> {
        boolean test(FixedRekord<T> rekord);
    }
}
