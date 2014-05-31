package com.noodlesandwich.rekord.keys;

import java.util.Collections;
import java.util.Iterator;
import com.noodlesandwich.rekord.implementation.AbstractKey;

public abstract class DelegatingKey<T, V, W> extends AbstractKey<T, W> {
    private final Key<T, V> key;

    public DelegatingKey(String name, Key<T, V> key) {
        super(name);
        this.key = key;
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return Collections.<Key<? super T, ?>>singleton(key).iterator();
    }
}
