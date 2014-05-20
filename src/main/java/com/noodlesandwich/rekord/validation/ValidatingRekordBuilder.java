package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.implementation.Keys;
import com.noodlesandwich.rekord.keys.KeySet;
import com.noodlesandwich.rekord.properties.Properties;
import org.hamcrest.Matcher;

public final class ValidatingRekordBuilder {
    private ValidatingRekordBuilder() { }

    public static final class UnkeyedRekord<T> {
        private final String name;

        public UnkeyedRekord(String name) {
            this.name = name;
        }

        // CHECKSTYLE:OFF
        @SafeVarargs
        public final UnsureRekord<T> accepting(KeySet<? super T>... keys) {
            @SuppressWarnings("varargs")
            List<KeySet<? super T>> keyList = Arrays.asList(keys);
            return accepting(Keys.from(keyList));
        }
        // CHECKSTYLE:ON

        public UnsureRekord<T> accepting(KeySet<T> keys) {
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
