package com.noodlesandwich.rekord.properties;

import com.noodlesandwich.rekord.keys.Key;

public interface Property<T, V> {
    Key<? super T, V> key();

    V value();
}
