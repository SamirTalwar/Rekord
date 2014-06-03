package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.PropertyMap;

public final class ValidRekord<T> extends AbstractFixedRekord<T> {
    ValidRekord(String name, Keys<T> acceptedKeys, PropertyMap<T> properties) {
        super(name, acceptedKeys, properties);
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
