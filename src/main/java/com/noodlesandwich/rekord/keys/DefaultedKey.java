package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Key;
import com.noodlesandwich.rekord.Properties;

public final class DefaultedKey<T, V> extends Key<T, V> {
    private final Key<T, V> delegate;
    private final V defaultValue;

    public DefaultedKey(Key<T, V> delegate, V defaultValue) {
        this.delegate = delegate;
        this.defaultValue = defaultValue;
    }

    @Override
    public V retrieveFrom(Properties<T> properties, Key<T, V> surrogateKey) {
        V value = delegate.retrieveFrom(properties, surrogateKey);
        return value != null ? value : defaultValue;
    }

    @Override
    public String toString() {
        return delegate.toString();
    }
}
