package com.noodlesandwich.rekord.keys;

import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;
import com.noodlesandwich.rekord.serialization.Serializer;

public abstract class DelegatingKey<T, V> extends AbstractKey<T, V> {
    private final Keys<T> keys;

    public DelegatingKey(String name, Keys<T> keys) {
        super(name);
        this.keys = keys;
    }

    @Override
    public final <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) throws E {
        for (Key<? super T, ?> key : keys) {
            @SuppressWarnings("unchecked")
            Key<? super T, Object> castKey = (Key<? super T, Object>) key;
            castKey.accumulate(value, accumulator);
        }
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return keys.iterator();
    }
}
