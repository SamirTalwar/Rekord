package com.noodlesandwich.rekord.keys;

import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.serialization.Deserializer;
import com.noodlesandwich.rekord.serialization.Serializer;

public abstract class DelegatingKey<T, V> extends AbstractKey<T, V> {
    private final Keys<T> keys;

    public DelegatingKey(String name, Keys<T> keys) {
        super(name);
        this.keys = keys;
    }

    @Override
    public final <A, E extends Exception> void serialize(V value, Serializer.Accumulator<A, E> accumulator) throws E {
        for (Key<T, ?> key : keys) {
            @SuppressWarnings("unchecked")
            Key<T, Object> castKey = (Key<T, Object>) key;
            castKey.serialize(value, accumulator);
        }
    }

    @Override
    public final <R, E extends Exception> void deserialize(Object value, Deserializer.Accumulator<T, R, E> accumulator) throws E {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Iterator<Key<T, ?>> iterator() {
        return keys.iterator();
    }
}
