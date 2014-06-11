package com.noodlesandwich.rekord.keys;

public interface BuildableKey<T, V, B> extends Key<T, V> {
    B builder();
}
