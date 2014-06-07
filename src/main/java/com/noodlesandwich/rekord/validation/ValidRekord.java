package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.implementation.AbstractFixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;

public final class ValidRekord<T> extends AbstractFixedRekord<T> {
    ValidRekord(String name, Keys<T> acceptedKeys, Properties<T> properties) {
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

    @Override
    public <V> Rekord<T> with(Property<? super T, V> property) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Rekord<T> merge(Rekord<T> other) {
        throw new UnsupportedOperationException();
    }
}
