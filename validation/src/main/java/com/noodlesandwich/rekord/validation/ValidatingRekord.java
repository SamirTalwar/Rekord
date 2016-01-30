package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.RekordBuilder;
import com.noodlesandwich.rekord.RekordTemplate;
import com.noodlesandwich.rekord.implementation.KeySet;
import com.noodlesandwich.rekord.implementation.PersistentProperties;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyKeys;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public final class ValidatingRekord<T> implements RekordBuilder<T, ValidatingRekord<T>> {
    private final String name;
    private final Keys<T> acceptedKeys;
    private final Properties<T> properties;
    private final Matcher<FixedRekord<T>> matcher;

    private ValidatingRekord(String name, Keys<T> acceptedKeys, Properties<T> properties, Matcher<FixedRekord<T>> matcher) {
        this.name = name;
        this.acceptedKeys = acceptedKeys;
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
        return acceptedKeys;
    }

    @Override
    public <V> Key<T, V> keyNamed(String nameToLookup) throws KeyNotFoundException {
        return acceptedKeys.keyNamed(nameToLookup);
    }

    @Override
    public <V> ValidatingRekord<T> with(Property<T, V> property) {
        return set(properties.set(property));
    }

    @Override
    public <V> ValidatingRekord<T> with(Key<T, V> key, V value) {
        return set(key.set(value, properties));
    }

    @Override
    public <V> ValidatingRekord<T> with(V value, Key<T, V> key) {
        return set(key.set(value, properties));
    }

    @Override
    public ValidatingRekord<T> without(Key<T, ?> key) {
        return set(properties.remove(key));
    }

    private ValidatingRekord<T> set(Properties<T> newProperties) {
        PropertyKeys.checkAcceptabilityOf(newProperties, acceptedKeys);
        return new ValidatingRekord<>(name, acceptedKeys, newProperties, matcher);
    }

    @Override
    public ValidatingRekord<T> merge(FixedRekord<T> other) {
        ValidatingRekord<T> result = this;
        for (Property<T, ?> property : other.properties()) {
            result = result.with(property);
        }
        return result;
    }

    public ValidRekord<T> fix() throws InvalidRekordException {
        ValidRekord<T> rekord = new ValidRekord<>(name, acceptedKeys, properties);
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
        @SuppressWarnings("varargs")
        @SafeVarargs
        public final UnsureRekord<T> accepting(Keys<T>... keys) {
            return accepting(KeySet.from(keys));
        }
        // CHECKSTYLE:ON

        public UnsureRekord<T> accepting(Keys<T> keys) {
            return new UnsureRekord<>(name, keys);
        }
    }

    public static final class UnsureRekord<T> {
        private final String name;
        private final Keys<T> acceptedKeys;

        private UnsureRekord(String name, Keys<T> acceptedKeys) {
            this.name = name;
            this.acceptedKeys = acceptedKeys;
        }

        public ValidatingRekord<T> expecting(Matcher<FixedRekord<T>> matcher) {
            return new ValidatingRekord<>(name, acceptedKeys, new PersistentProperties<T>(), matcher);
        }
    }
}
