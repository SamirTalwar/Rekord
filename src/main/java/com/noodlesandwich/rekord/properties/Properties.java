package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface Properties<T> extends Iterable<Property<? super T, ?>> {
    boolean has(Key<? super T, ?> key);

    <V> V get(Key<? super T, V> key);

    Properties<T> set(Property<? super T, ?> property);

    Properties<T> remove(Key<? super T, ?> key);
}
