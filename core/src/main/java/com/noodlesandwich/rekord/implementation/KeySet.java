package com.noodlesandwich.rekord.implementation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class KeySet<T> implements Keys<T> {
    private final PSet<Key<T, ?>> keys;

    private KeySet(PSet<Key<T, ?>> keys) {
        this.keys = keys;
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Keys<T> from(Keys<T>... keys) {
        return from(Arrays.asList(keys));
    }

    public static <T> Keys<T> from(Collection<Keys<T>> keys) {
        PSet<Key<T, ?>> result = OrderedPSet.empty();
        for (Keys<T> innerKeys : keys) {
            for (Key<T, ?> key : innerKeys) {
                result = result.plus(key);
            }
        }
        return new KeySet<>(result);
    }

    @Override
    public Iterator<Key<T, ?>> iterator() {
        return keys.iterator();
    }

    @Override
    public boolean contains(Key<T, ?> key) {
        return keys.contains(key);
    }

    @Override
    public Set<Key<T, ?>> toSet() {
        return keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof KeySet)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        KeySet<T> that = (KeySet<T>) o;
        return this.keys.equals(that.keys);

    }

    @Override
    public int hashCode() {
        return keys.hashCode();
    }
}
