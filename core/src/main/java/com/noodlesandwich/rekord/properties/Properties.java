package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface Properties<T> extends Iterable<Property<T, ?>> {
    boolean has(Key<T, ?> key);

    <V> V get(Key<T, V> key);

    Properties<T> set(Property<T, ?> property);

    Properties<T> remove(Key<T, ?> key);
}
