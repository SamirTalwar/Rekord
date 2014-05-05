package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Rekord;
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
            return new ValidatingRekord<>(name, properties);
        }
    }

    public static final class ValidatingRekord<T> implements RekordBuilder<T, ValidatingRekord<T>> {
        private final String name;
        private final Properties properties;

        public ValidatingRekord(String name, Properties properties) {
            this.name = name;
            this.properties = properties;
        }

        @Override
        public <V> ValidatingRekord<T> with(Key<? super T, V> key, V value) {
            return new ValidatingRekord<>(name, key.storeTo(properties, value));
        }

        @Override
        public <V> ValidatingRekord<T> with(V value, Key<? super T, V> key) {
            return with(key, value);
        }

        @Override
        public ValidatingRekord<T> without(Key<? super T, ?> key) {
            return new ValidatingRekord<>(name, properties.without(key));
        }

        public FixedRekord<T> fix() {
            return new Rekord<>(name, properties);
        }
    }

    public static interface Validator<T> {
        boolean test(Rekord<T> rekord);
    }
}
