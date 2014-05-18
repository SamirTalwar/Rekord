package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.properties.Properties;

public final class ValidRekord<T> extends AbstractFixedRekord<T> {
    public ValidRekord(String name, Properties<T> properties) {
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
