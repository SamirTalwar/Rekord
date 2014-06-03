package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface PropertyMap<T> extends Properties<T> {
    boolean has(Key<? super T, ?> key);

    <V> V get(Key<? super T, V> key);

    PropertyMap<T> set(Property<? super T, ?> property);

    PropertyMap<T> remove(Key<? super T, ?> key);
}
