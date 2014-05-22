package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.LimitedPropertyMap;
import com.noodlesandwich.rekord.keys.Keys;
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
        public final UnsureRekord<T> accepting(Keys<? super T>... keys) {
            @SuppressWarnings("varargs")
            List<Keys<? super T>> keyList = Arrays.asList(keys);
            return accepting(KeySet.from(keyList));
        }
        // CHECKSTYLE:ON

        public UnsureRekord<T> accepting(Keys<T> keys) {
            return new UnsureRekord<>(name, new LimitedPropertyMap<>(keys));
        }
    }

    public static final class UnsureRekord<T> {
        private final String name;
        private final LimitedPropertyMap<T> properties;

        public UnsureRekord(String name, LimitedPropertyMap<T> properties) {
            this.name = name;
            this.properties = properties;
        }

        public ValidatingRekord<T> expecting(Matcher<FixedRekord<T>> matcher) {
            return new ValidatingRekord<>(name, properties, matcher);
        }
    }
}
