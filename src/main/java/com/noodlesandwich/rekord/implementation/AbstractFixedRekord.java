package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;
import org.pcollections.PSet;

public abstract class AbstractFixedRekord<T> implements FixedRekord<T> {
    private final String name;
    private final Properties<T> properties;

    public AbstractFixedRekord(String name, Properties<T> properties) {
        this.name = name;
        this.properties = properties;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public <V> V get(Key<? super T, V> key) {
        return key.retrieveFrom(properties);
    }

    @Override
    public boolean has(Key<T, ?> key) {
        return properties.has(key);
    }

    @Override
    public PSet<Key<? super T, ?>> keys() {
        return properties.keys();
    }

    @Override
    public PSet<Key<? super T, ?>> acceptedKeys() {
        return properties.acceptedKeys();
    }

    @Override
    public <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E {
        return serializer.serialize(this);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof AbstractFixedRekord)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        AbstractFixedRekord<T> that = (AbstractFixedRekord<T>) other;
        return properties.equals(that.properties);
    }

    @Override
    public int hashCode() {
        return properties.hashCode();
    }

    @Override
    public String toString() {
        return serialize(new StringSerializer());
    }
}
