package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface PropertyMap<T> extends Properties<T> {
    <V> V get(Key<? super T, V> key);

    boolean has(Key<? super T, ?> key);

    PropertyMap<T> set(Property<? super T, ?> property);

    PropertyMap<T> remove(Key<? super T, ?> key);
}
