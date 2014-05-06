package com.noodlesandwich.rekord;

import org.pcollections.PSet;

public interface RekordBuilder<T, B extends RekordBuilder<T, B>> extends Named {
    PSet<Key<? super T, ?>> acceptedKeys();

    <V> B with(Key<? super T, V> key, V value);

    <V> B with(V value, Key<? super T, V> key);

    B without(Key<? super T, ?> key);
}
