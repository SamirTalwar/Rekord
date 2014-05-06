package com.noodlesandwich.rekord;

import org.pcollections.PSet;

public interface RekordTemplate<T> extends Named {
    PSet<Key<? super T, ?>> acceptedKeys();
}
