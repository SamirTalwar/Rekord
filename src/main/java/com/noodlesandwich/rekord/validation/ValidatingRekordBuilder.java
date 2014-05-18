package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.properties.Properties;
import org.hamcrest.Matcher;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class ValidatingRekordBuilder {
    private ValidatingRekordBuilder() { }

    public static final class UnkeyedRekord<T> {
        private final String name;

        public UnkeyedRekord(String name) {
            this.name = name;
        }

        // CHECKSTYLE:OFF
        @SafeVarargs
        public final UnsureRekord<T> accepting(Key<? super T, ?>... keys) {
            @SuppressWarnings("varargs")
            List<Key<? super T, ?>> keyList = Arrays.asList(keys);
            return accepting(OrderedPSet.from(keyList));
        }
        // CHECKSTYLE:ON

        public UnsureRekord<T> accepting(PSet<Key<? super T, ?>> keys) {
            return new UnsureRekord<>(name, new Properties<>(keys));
        }
    }

    public static final class UnsureRekord<T> {
        private final String name;
        private final Properties<T> properties;

        public UnsureRekord(String name, Properties<T> properties) {
            this.name = name;
            this.properties = properties;
        }

        public ValidatingRekord<T> expecting(Matcher<FixedRekord<T>> matcher) {
            return new ValidatingRekord<>(name, properties, matcher);
        }
    }
}
