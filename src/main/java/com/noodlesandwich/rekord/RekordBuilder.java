package com.noodlesandwich.rekord;

public interface RekordBuilder<T, B extends RekordBuilder<T, B>> extends RekordTemplate<T> {
    <V> B with(Key<? super T, V> key, V value);

    <V> B with(V value, Key<? super T, V> key);

    B without(Key<? super T, ?> key);
}
