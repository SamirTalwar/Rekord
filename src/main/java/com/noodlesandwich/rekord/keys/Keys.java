package com.noodlesandwich.rekord.keys;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.pcollections.OrderedPSet;
import org.pcollections.PSet;

public final class Keys<T> implements KeySet<T> {
    private final PSet<Key<? super T, ?>> keys;

    private Keys(PSet<Key<? super T, ?>> keys) {
        this.keys = keys;
    }

    @SafeVarargs
    public static <T> KeySet<T> from(KeySet<? super T>... keys) {
        @SuppressWarnings("varargs")
        List<KeySet<? super T>> keyList = Arrays.asList(keys);
        return from(keyList);
    }

    public static <T> KeySet<T> from(Collection<KeySet<? super T>> keys) {
        PSet<Key<? super T, ?>> result = OrderedPSet.empty();
        for (KeySet<? super T> keySet : keys) {
            for (Key<? super T, ?> key : keySet) {
                result = result.plus(key);
            }
        }
        return new Keys<>(result);
    }

    @Override
    public KeySet<T> originals() {
        PSet<Key<? super T, ?>> originalKeys = OrderedPSet.empty();
        for (Key<? super T, ?> key : keys) {
            originalKeys = originalKeys.plus(key.original());
        }
        return new Keys<>(originalKeys);
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

        if (!(o instanceof Keys)) {
            return false;
        }

        @SuppressWarnings("unchecked")
        Keys<T> that = (Keys<T>) o;
        return this.keys.equals(that.keys);

    }

    @Override
    public int hashCode() {
        return keys.hashCode();
    }
}
