package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.RekordBuilder;
import com.noodlesandwich.rekord.RekordTemplate;
import org.pcollections.PSet;

public final class ValidatingRekord<T> implements RekordBuilder<T, ValidatingRekord<T>> {
    private final String name;
    private final Properties properties;
    private final Validator<T> validator;

    public ValidatingRekord(String name, Properties properties, Validator<T> validator) {
        this.name = name;
        this.properties = properties;
        this.validator = validator;
    }

    public static <T> ValidatingRekordBuilder.UnkeyedRekord<T> of(Class<T> type) {
        return create(type.getSimpleName());
    }

    public static <T> ValidatingRekordBuilder.UnkeyedRekord<T> create(String name) {
        return new ValidatingRekordBuilder.UnkeyedRekord<>(name);
    }

    public static <T> ValidatingRekordBuilder.UnsureRekord<T> validating(RekordTemplate<T> rekord) {
        return ValidatingRekord.<T>create(rekord.name()).accepting(rekord.acceptedKeys());
    }

    @Override
    public String name() {
        return name;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PSet<Key<? super T, ?>> acceptedKeys() {
        return (PSet) properties.acceptedKeys();
    }

    @Override
    public <V> ValidatingRekord<T> with(Key<? super T, V> key, V value) {
        return new ValidatingRekord<>(name, key.storeTo(properties, value), validator);
    }

    @Override
    public <V> ValidatingRekord<T> with(V value, Key<? super T, V> key) {
        return with(key, value);
    }

    @Override
    public ValidatingRekord<T> without(Key<? super T, ?> key) {
        return new ValidatingRekord<>(name, properties.without(key), validator);
    }

    public FixedRekord<T> fix() throws InvalidRekordException {
        Rekord<T> rekord = new Rekord<>(name, properties);
        validator.test(rekord);
        return rekord;
    }
}
