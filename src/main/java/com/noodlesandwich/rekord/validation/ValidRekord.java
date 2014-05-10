package com.noodlesandwich.rekord.validation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.implementation.FixedRekordDelegate;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;
import org.pcollections.PSet;

public final class ValidRekord<T> implements FixedRekord<T> {
    private final FixedRekord<T> delegate;

    public ValidRekord(String name, Properties<T> properties) {
        this.delegate = new FixedRekordDelegate<>(name, properties);
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public <V> V get(Key<? super T, V> key) {
        return delegate.get(key);
    }

    @Override
    public boolean has(Key<T, ?> key) {
        return delegate.has(key);
    }

    @Override
    public PSet<Key<? super T, ?>> keys() {
        return delegate.keys();
    }

    @Override
    public PSet<Key<? super T, ?>> acceptedKeys() {
        return delegate.acceptedKeys();
    }

    @Override
    public <R> R serialize(Serializer<R> serializer) {
        return delegate.serialize(serializer);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof ValidRekord)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        ValidRekord<T> that = (ValidRekord<T>) other;
        return delegate.equals(that.delegate);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
