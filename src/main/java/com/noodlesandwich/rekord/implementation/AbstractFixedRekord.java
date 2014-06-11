package com.noodlesandwich.rekord.implementation;

import java.util.Objects;
import com.noodlesandwich.rekord.Rekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.keys.RekordKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.PropertyKeys;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;

public abstract class AbstractFixedRekord<T> implements Rekord<T> {
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

    public final boolean has(Key<? super T, ?> key) {
        return key.test(properties);
    }

    public final <V> V get(Key<? super T, V> key) {
        return key.get(properties);
    }

    public final Keys<T> keys() {
        return PropertyKeys.keysFrom(properties);
    }

    public final Properties<T> properties() {
        return properties;
    }

    public final <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E {
        return serializer.serialize(RekordKey.named(name()).builtFrom(this), this);
    }

    protected final boolean abstractEquals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof AbstractFixedRekord)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        AbstractFixedRekord<T> that = (AbstractFixedRekord<T>) other;
        return name.equals(that.name) && properties.equals(that.properties);

    }

    protected final int abstractHashCode() {
        return Objects.hash(name, properties);
    }

    @Override
    public final String toString() {
        return serialize(new StringSerializer());
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
