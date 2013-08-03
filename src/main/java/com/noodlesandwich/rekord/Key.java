package com.noodlesandwich.rekord;

public final class Key<T, V> {
    private Key() { }

    public static <T, V> Key<T, V> key() {
        return new Key<>();
    }
}
