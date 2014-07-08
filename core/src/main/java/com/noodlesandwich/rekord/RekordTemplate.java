package com.noodlesandwich.rekord;

import com.noodlesandwich.rekord.keys.Keys;

public interface RekordTemplate<T> extends Named {
    Keys<T> acceptedKeys();
}
