package com.noodlesandwich.rekord.keys;

import java.util.Map;
import com.noodlesandwich.rekord.Key;

public final class OrdinaryKey<T, V> extends NamedKey<T, V> {
    public OrdinaryKey(String name) {
        super(name);
    }

    @SuppressWarnings("unchecked")
    @Override
    public V retrieveFrom(Map<Key<? super T, ?>, Object> properties) {
        return (V) properties.get(this);
    }

    @Override
    public boolean isContainedIn(Map<Key<? super T, ?>, Object> properties) {
        return properties.containsKey(this);
    }
}
