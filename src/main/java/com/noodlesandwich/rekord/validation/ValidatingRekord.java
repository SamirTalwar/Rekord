package com.noodlesandwich.rekord.validation;

import java.util.Arrays;
import java.util.List;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.RekordBuilder;
import com.noodlesandwich.rekord.RekordTemplate;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.LimitedPropertyMap;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Property;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public final class ValidatingRekord<T> implements RekordBuilder<T, ValidatingRekord<T>> {
    private final String name;
    private final LimitedPropertyMap<T> properties;
    private final Matcher<FixedRekord<T>> matcher;

    private ValidatingRekord(String name, LimitedPropertyMap<T> properties, Matcher<FixedRekord<T>> matcher) {
        this.name = name;
        this.properties = properties;
        this.matcher = matcher;
    }

    public static <T> UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> UnkeyedRekord<T> create(String name) {
        return new UnkeyedRekord<>(name);
    }

    public static <T> UnsureRekord<T> validating(RekordTemplate<T> rekord) {
        return ValidatingRekord.<T>create(rekord.name()).accepting(rekord.acceptedKeys());
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public Keys<T> acceptedKeys() {
        return properties.acceptedKeys();
    }

    @Override
    public <V> ValidatingRekord<T> with(Property<? super T, V> property) {
        return new ValidatingRekord<>(name, properties.with(property), matcher);
    }

    @Override
    public <V> ValidatingRekord<T> with(Key<? super T, V> key, V value) {
        return with(key.of(value));
    }

    @Override
    public <V> ValidatingRekord<T> with(V value, Key<? super T, V> key) {
        return with(key.of(value));
    }

    @Override
    public ValidatingRekord<T> without(Key<? super T, ?> key) {
        return new ValidatingRekord<>(name, properties.without(key), matcher);
    }

    public ValidRekord<T> fix() throws InvalidRekordException {
        ValidRekord<T> rekord = new ValidRekord<>(name, properties);
        if (!matcher.matches(rekord)) {
            StringDescription description = new StringDescription();
            description.appendText("Expected that ").appendDescriptionOf(matcher).appendText(", but ");
            matcher.describeMismatch(rekord, description);
            description.appendText(".");
            throw new InvalidRekordException(description.toString());
        }
        return rekord;
    }

    public static final class UnkeyedRekord<T> {
        private final String name;

        private UnkeyedRekord(String name) {
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

        private UnsureRekord(String name, LimitedPropertyMap<T> properties) {
            this.name = name;
            this.properties = properties;
        }

        public ValidatingRekord<T> expecting(Matcher<FixedRekord<T>> matcher) {
            return new ValidatingRekord<>(name, properties, matcher);
        }
    }
}
