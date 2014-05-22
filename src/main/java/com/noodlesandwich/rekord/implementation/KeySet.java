package com.noodlesandwich.rekord.implementation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class KeySet<T> implements Keys<T> {
    private final PSet<Key<? super T, ?>> keys;

    private KeySet(PSet<Key<? super T, ?>> keys) {
        this.keys = keys;
    }

    @SafeVarargs
    public static <T> Keys<T> from(Keys<? super T>... keys) {
        @SuppressWarnings("varargs")
        List<Keys<? super T>> keyList = Arrays.asList(keys);
        return from(keyList);
    }

    public static <T> Keys<T> from(Collection<Keys<? super T>> keys) {
        PSet<Key<? super T, ?>> result = OrderedPSet.empty();
        for (Keys<? super T> innerKeys : keys) {
            for (Key<? super T, ?> key : innerKeys) {
                result = result.plus(key);
            }
        }
        return new KeySet<>(result);
    }

    @Override
    public Iterator<Key<? super T, ?>> iterator() {
        return keys.iterator();
    }

    @Override
    public boolean contains(Key<? super T, ?> key) {
        return keys.contains(key);
    }

    @Override
    public Set<Key<? super T, ?>> toSet() {
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
