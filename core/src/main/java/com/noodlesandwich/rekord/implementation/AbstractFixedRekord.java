package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.PropertyKeys;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;

public abstract class AbstractFixedRekord<T> implements FixedRekord<T> {
    private final String name;
    private final Keys<T> acceptedKeys;
    private final Properties<T> properties;

    protected AbstractFixedRekord(String name, Keys<T> acceptedKeys, Properties<T> properties) {
        this.name = name;
        this.acceptedKeys = acceptedKeys;
        this.properties = properties;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final Keys<T> acceptedKeys() {
        return acceptedKeys;
    }

    @Override
    public final boolean has(Key<? super T, ?> key) {
        return key.test(properties);
    }

    @Override
    public final <V> V get(Key<? super T, V> key) {
        return key.get(properties);
    }

    @Override
    public final Keys<T> keys() {
        return PropertyKeys.keysFrom(properties);
    }

    @Override
    public final Properties<T> properties() {
        return properties;
    }

    @Override
    public final <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E {
        return serializer.serialize(name, this);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public final String toString() {
        return serialize(new StringSerializer());
    }
}
