package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.buildables.Buildable;

public interface BuildableKey<T, V> extends Key<T, V>, Buildable<V> {
}
