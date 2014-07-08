package com.noodlesandwich.rekord.keys;

public interface BuildableKey<T, V> extends Key<T, V> {
    V builder();
}
