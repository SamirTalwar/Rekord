package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface PropertyMap<T> {
    <V> V get(Key<? super T, V> key);

    boolean has(Key<? super T, ?> key);
}
