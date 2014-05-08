package com.noodlesandwich.rekord.implementation;

import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.serialization.RekordSerializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;
import org.pcollections.PSet;

public final class FixedRekordDelegate<T> implements FixedRekord<T> {
    private final String name;
    private final Properties<T> properties;

    public FixedRekordDelegate(String name, Properties<T> properties) {
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
    public <A, R> R serialize(RekordSerializer<A, R> serializer) {
        RekordSerializer.Serializer<A> internalSerializer = serializer.start(name);
        accumulateIn(internalSerializer);
        return serializer.finish(internalSerializer);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> void accumulateIn(RekordSerializer.Serializer<A> serializer) {
        for (Key<? super T, ?> key : keys()) {
            Key<? super T, Object> castKey = (Key<? super T, Object>) key;
            Object value = castKey.retrieveFrom(properties);
            castKey.accumulate(value, serializer);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof FixedRekordDelegate)) {
            return false;
        }

        FixedRekordDelegate<T> that = (FixedRekordDelegate<T>) other;
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
