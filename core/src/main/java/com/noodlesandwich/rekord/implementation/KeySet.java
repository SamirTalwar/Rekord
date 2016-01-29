package com.noodlesandwich.rekord.implementation;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;
import org.pcollections.HashTreePMap;
import org.pcollections.OrderedPSet;
import org.pcollections.PMap;
import org.pcollections.PSet;

public final class KeySet<T> implements Keys<T> {
    private final PSet<Key<T, ?>> keys;
    private final PMap<String, Key<T, ?>> keysByName;

    private KeySet(PSet<Key<T, ?>> keys, PMap<String, Key<T, ?>> keysByName) {
        this.keys = keys;
        this.keysByName = keysByName;
    }

    @SuppressWarnings("varargs")
    @SafeVarargs
    public static <T> Keys<T> from(Keys<T>... keys) {
        return from(Arrays.asList(keys));
    }

    public static <T> Keys<T> from(Collection<Keys<T>> keys) {
        PSet<Key<T, ?>> keySet = OrderedPSet.empty();
        PMap<String, Key<T, ?>> keysByName = HashTreePMap.empty();
        for (Keys<T> innerKeys : keys) {
            for (Key<T, ?> key : innerKeys) {
                keySet = keySet.plus(key);
                keysByName = keysByName.plus(key.name(), key);
            }
        }
        return new KeySet<>(keySet, keysByName);
    }

    @Override
    public Iterator<Key<T, ?>> iterator() {
        return keys.iterator();
    }

    @Override
    public boolean contains(Key<T, ?> key) {
        return keys.contains(key);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <V> Key<T, V> keyNamed(String nameToLookup) {
        Key<T, V> key = (Key<T, V>) keysByName.get(nameToLookup);
        if (key == null) {
            throw new IllegalArgumentException(String.format("The key \"%s\" does not exist.", nameToLookup));
        }
        return key;
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
