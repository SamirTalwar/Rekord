package com.noodlesandwich.rekord;

@SuppressWarnings("UnusedDeclaration")
public final class Key<T, V> {
    private final String name;

    private Key(String name) {
        this.name = name;
    }

    public static <T, V> Key<T, V> named(String name) {
        return new Key<>(name);
    }

    @Override
    public String toString() {
        return name;
    }
}
