package com.noodlesandwich.rekord.implementation;

import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import com.noodlesandwich.rekord.transformers.Transformer;

public abstract class AbstractKey<T, V> implements Key<T, V> {
    @Override
    public final <NewV> Key<T, NewV> then(Transformer<V, NewV> transformer) {
        return that(transformer);
    }

    @Override
    public final String toString() {
        return name();
    }

    @Override
    public final Keys<T> originals() {
        return original();
    }

    @Override
    public final Iterator<Key<? super T, ?>> iterator() {
        return Collections.<Key<? super T, ?>>singleton(this).iterator();
    }

    @Override
    public final boolean contains(Key<? super T, ?> key) {
        return equals(key);
    }

    @Override
    public final Set<Key<? super T, ?>> toSet() {
        return Collections.<Key<? super T, ?>>singleton(this);
    }
}
