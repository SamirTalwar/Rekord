package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.implementation.LimitedPropertyMap;

public final class ValidRekord<T> extends AbstractFixedRekord<T> {
    ValidRekord(String name, LimitedPropertyMap<T> properties) {
        super(name, properties);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ValidRekord && abstractEquals(other);
    }

    @Override
    public int hashCode() {
        return abstractHashCode();
    }
}
