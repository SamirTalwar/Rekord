package com.noodlesandwich.rekord.implementation;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import com.noodlesandwich.rekord.FixedRekord;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.keys.RekordKey;
import com.noodlesandwich.rekord.properties.Properties;
import com.noodlesandwich.rekord.properties.Property;
import com.noodlesandwich.rekord.properties.PropertyMap;
import com.noodlesandwich.rekord.serialization.Serializer;
import com.noodlesandwich.rekord.serialization.StringSerializer;

public abstract class AbstractFixedRekord<T> implements FixedRekord<T> {
    private final String name;
    private final Keys<T> acceptedKeys;
    private final PropertyMap<T> properties;

    protected AbstractFixedRekord(String name, Keys<T> acceptedKeys, PropertyMap<T> properties) {
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
        Set<Keys<? super T>> keys = new HashSet<>();
        for (Property<? super T, ?> property : properties) {
            Key<? super T, ?> key = property.key();
            if (key.test(properties)) {
                keys.add(key);
            }
        }
        return KeySet.from(keys);
    }

    @Override
    public final Properties<T> properties() {
        return properties;
    }

    @Override
    public final <R, E extends Exception> R serialize(Serializer<R, E> serializer) throws E {
        return serializer.serialize(RekordKey.<T, T>named(name()), this);
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
}
