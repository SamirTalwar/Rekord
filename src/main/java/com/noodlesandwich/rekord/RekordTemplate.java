package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.Key;
import org.pcollections.PSet;

public interface RekordTemplate<T> extends Named {
    PSet<Key<? super T, ?>> acceptedKeys();
}
