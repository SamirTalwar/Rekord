package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface Properties<T> extends Iterable<Property<? super T, ?>> {
    <V> V get(Key<? super T, V> key);
}
