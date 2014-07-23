package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyKeys;
import com.noodlesandwich.rekord.serialization.Serializer;

public final class LimitedRekord<T> implements Rekord<T> {
    private final FixedRekord<T> delegate;
    private final Keys<T> acceptedKeys;
    private final Properties<T> properties;

    public LimitedRekord(String name, Keys<T> acceptedKeys, Properties<T> properties) {
        this.delegate = new PropertyBackedFixedRekord<>(name, acceptedKeys, properties);
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
    }

    @Override
    public String name() {
        return delegate.name();
    }

    @Override
    public boolean has(Key<? super T, ?> key) {
        return delegate.has(key);
    }

    @Override
    public <V> V get(Key<? super T, V> key) {
        return delegate.get(key);
    }

    @Override
    public <V> Rekord<T> with(Property<? super T, V> property) {
        return set(properties.set(property));
    }

    @Override
    public <V> Rekord<T> with(Key<? super T, V> key, V value) {
        return set(key.set(value, properties));
    }

    @Override
    public <V> Rekord<T> with(V value, Key<? super T, V> key) {
        return set(key.set(value, properties));
    }

    @Override
    public Rekord<T> without(Key<? super T, ?> key) {
        return set(properties.remove(key));
    }

    private Rekord<T> set(Properties<T> newProperties) {
        PropertyKeys.checkAcceptabilityOf(newProperties, acceptedKeys);
        return new LimitedRekord<>(name(), acceptedKeys, newProperties);
    }

    @Override
    public Rekord<T> merge(FixedRekord<T> other) {
        Rekord<T> result = this;
        for (Property<? super T, ?> property : other.properties()) {
            result = result.with(property);
        }
        return result;
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
    public Properties<T> properties() {
        return delegate.properties();
    }

    @Override
    public <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E {
        return delegate.serialize(serializer);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof Rekord && FixedRekordHelpers.equals(this, other);
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
