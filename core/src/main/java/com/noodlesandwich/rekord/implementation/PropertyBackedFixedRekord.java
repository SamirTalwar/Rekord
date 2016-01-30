package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.KeyNotFoundException;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.PropertyKeys;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;

public final class PropertyBackedFixedRekord<T> implements FixedRekord<T> {
    private final String name;
    private final Keys<T> acceptedKeys;
    private final Properties<T> properties;

    public PropertyBackedFixedRekord(String name, Keys<T> acceptedKeys, Properties<T> properties) {
        this.name = name;
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
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
    public boolean has(Key<T, ?> key) {
        return key.test(properties);
    }

    @Override
    public <V> V get(Key<T, V> key) {
        return key.get(properties);
    }

    @Override
    public Keys<T> keys() {
        return PropertyKeys.keysFrom(properties);
    }

    @Override
    public Properties<T> properties() {
        return properties;
    }

    @Override
    public <S, E extends Exception> S serialize(Serializer<S, E> serializer) throws E {
        return serializer.serialize(name, this);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FixedRekord && FixedRekordHelpers.equals(this, o);
    }

    @Override
    public int hashCode() {
        return FixedRekordHelpers.hashCode(this);
    }

    @Override
    public String toString() {
        return serialize(new StringSerializer());
    }
}
