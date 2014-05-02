package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Rekord;

public final class RekordKey<T, R> extends OriginalKey<T, Rekord<R>> {
    private final String name;

    private RekordKey(String name) {
        this.name = name;
    }

    public static <T, R> Key<T, Rekord<R>> named(String name) {
        return new RekordKey<>(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
