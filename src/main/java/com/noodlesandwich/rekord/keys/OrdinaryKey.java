package com.noodlesandwich.rekord.keys;

import com.noodlesandwich.rekord.Properties;

public final class OrdinaryKey<T, V> extends NamedKey<T, V> {
    public OrdinaryKey(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V retrieveFrom(Properties<T> properties) {
        return properties.get(this);
    }

    @Override
    public boolean isContainedIn(Properties<T> properties) {
        return properties.contains(this);
    }
}
