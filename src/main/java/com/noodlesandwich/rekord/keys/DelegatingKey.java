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
    public final <A, E extends Exception> void accumulate(V value, Serializer.Accumulator<A, E> accumulator) {
        throw new UnsupportedOperationException();
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return keys.iterator();
    }
}
