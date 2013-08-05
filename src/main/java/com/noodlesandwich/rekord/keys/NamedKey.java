package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;

public final class NamedKey<T, V> extends Key<T, V> {
    private final String name;

    public NamedKey(String name) {
        this.name = name;
    }

    @Override
    public V retrieveFrom(Properties<T> properties, Key<T, V> surrogateKey) {
        return properties.get(surrogateKey);
    }

    @Override
    public String toString() {
        return name;
    }
}
