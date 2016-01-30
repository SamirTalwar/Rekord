package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.implementation.FixedRekordHelpers;
import com.noodlesandwich.rekord.implementation.PropertyBackedFixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class ValidRekord<T> implements FixedRekord<T> {
    private final FixedRekord<T> delegate;

    ValidRekord(String name, Keys<T> acceptedKeys, Properties<T> properties) {
        this.delegate = new PropertyBackedFixedRekord<>(name, acceptedKeys, properties);
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public boolean has(Key<T, ?> key) {
        return delegate.has(key);
    }

    @Override
    public <V> V get(Key<T, V> key) {
        return delegate.get(key);
    }

    @Override
    public Keys<T> keys() {
        return delegate.keys();
    }

    @Override
    public Keys<T> acceptedKeys() {
        return delegate.acceptedKeys();
    }

    @Override
    public <V> Key<T, V> keyNamed(String nameToLookup) throws KeyNotFoundException {
        return delegate.keyNamed(nameToLookup);
    }

    @Override
    public Properties<T> properties() {
        return delegate.properties();
    }

    @Override
    public <S, E extends Exception> S serialize(Serializer<S, E> serializer) throws E {
        return delegate.serialize(serializer);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ValidRekord && FixedRekordHelpers.equals(this, other);
    }

    @Override
    public int hashCode() {
        return FixedRekordHelpers.hashCode(this);
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
