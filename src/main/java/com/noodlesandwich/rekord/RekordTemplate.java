package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.KeySet;

public interface RekordTemplate<T> extends Named {
    KeySet<T> acceptedKeys();
}
