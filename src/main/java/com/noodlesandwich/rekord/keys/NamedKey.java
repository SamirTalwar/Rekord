package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;

public final class NamedKey<T, V> extends Key<T, V> {
    private final Key<T, V> delegate;
    private final String name;

    public NamedKey(Key<T, V> delegate, String name) {
        this.delegate = delegate;
        this.name = name;
    }

    @Override
    public V retrieveFrom(Properties<T> properties, Key<T, V> surrogateKey) {
        return delegate.retrieveFrom(properties, surrogateKey);
    }

    @Override
    public String toString() {
        return name;
    }
}
