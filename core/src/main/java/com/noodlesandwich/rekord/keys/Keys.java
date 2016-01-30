package com.noodlesandwich.rekord.keys;

import java.util.Set;

public interface Keys<T> extends Iterable<Key<T, ?>> {
    boolean contains(Key<T, ?> key);

    <V> Key<T, V> keyNamed(String nameToLookup) throws KeyNotFoundException;

    Set<Key<T, ?>> toSet();
}
