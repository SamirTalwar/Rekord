package com.noodlesandwich.rekord.keys;

import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;

public abstract class DelegatingKey<T, V> extends AbstractKey<T, V> {
    private final Keys<T> keys;

    public DelegatingKey(String name, Keys<T> keys) {
        super(name);
        this.keys = keys;
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return keys.iterator();
    }
}
