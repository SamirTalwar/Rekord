package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.Key;
import com.noodlesandwich.rekord.keys.Keys;

public interface RekordTemplate<T> extends Named {
    Keys<T> acceptedKeys();

    <V> Key<T, V> keyNamed(String nameToLookup);
}
