package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.RekordBuilder;

public final class ValidatingRekord<T> implements RekordBuilder<T, ValidatingRekord<T>> {
    private final String name;
    private final Properties properties;
    private final Validator<T> validator;

    public ValidatingRekord(String name, Properties properties, Validator<T> validator) {
        this.name = name;
        this.properties = properties;
        this.validator = validator;
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
