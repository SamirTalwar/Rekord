package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;

public abstract class NamedKey<T, V> extends Key<T, V> {
    protected final String name;

    public NamedKey(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
